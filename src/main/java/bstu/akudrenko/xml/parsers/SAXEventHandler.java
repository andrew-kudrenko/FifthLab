package bstu.akudrenko.xml.parsers;

import java.lang.reflect.Field;
import java.util.Stack;
import java.util.function.Consumer;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import bstu.akudrenko.xml.bindings.BindXMLAttribute;
import bstu.akudrenko.xml.bindings.BindXMLEntity;
import bstu.akudrenko.xml.bindings.BindXMLNestedEntity;
import bstu.akudrenko.xml.bindings.BindXMLTag;

public class SAXEventHandler<M> extends DefaultHandler {
    private boolean shouldStop;
    private final Stack<String> elements = new Stack<>();
    private final Stack<Object> entities = new Stack<>();
    private final Stack<Field> fields = new Stack<>();
    private final Class<M> targetEntityClass;
    private Consumer<M> onEntityParsed;

    public SAXEventHandler(Class<M> cls) {
        targetEntityClass = cls;
    }

    public void setOnEntityParsed(Consumer<M> action) {
        onEntityParsed = action;
    }

    public void stop() {
        shouldStop = true;
    }

    @Override
    public void startDocument () {
        clear();
    }

    @Override
    public void startElement(String uri, String localName, String elementName, Attributes attributes) throws SAXStopParsingException {
        if (shouldStop) {
            throw new SAXStopParsingException();
        }

        elements.push(elementName);

        if (entities.isEmpty()) {
            var entityBinding = targetEntityClass.getAnnotation(BindXMLEntity.class);

            if (entityBinding != null && entityBinding.alias().equals(elementName)) {
                entities.push(createTargetEntityInstance());
            }
        } else {
            var last = entities.peek();

            for (var field : last.getClass().getDeclaredFields()) {
                if (isTagMatch(field, elementName)) {
                    fields.push(field);
                }

                var nestedEntityBinding = field.getDeclaredAnnotation(BindXMLNestedEntity.class);

                if (nestedEntityBinding != null && nestedEntityBinding.alias().equals(elementName)) {
                    try {
                        var instance = field.getType().getDeclaredConstructors()[0].newInstance();
                        field.set(entities.peek(), instance);
                        entities.push(instance);
                    } catch (Exception e) {
                        System.out.println("Initializing nested entity failed");
                    }
                }
            }
        }

        if (!entities.isEmpty()) {
            for (var field : entities.peek().getClass().getDeclaredFields()) {
                attachAttributes(field, attributes);
            }
        }
    }

    @Override
    public void endElement (String uri, String localName, String elementName) {
        var lastOpened = elements.pop();

        if (!entities.isEmpty()) {
            var lastBindEntityAnnotation = entities.peek().getClass().getDeclaredAnnotation(BindXMLEntity.class);

            if (lastBindEntityAnnotation != null && lastBindEntityAnnotation.alias().equals(elementName)) {
                var entity = entities.pop();

                if (entity.getClass().equals(targetEntityClass)) {
                    onEntityParsed.accept((M) entity);
                }
            }
        }

        if (!lastOpened.equals(elementName)) {
            throw new RuntimeException("Incorrect XML document structure");
        }
    }

    @Override
    public void characters (char ch[], int start, int length) {
        if (!fields.isEmpty() && !entities.isEmpty()) {
            var field = fields.pop();
            var rawValue = String.valueOf(ch).substring(start, start + length);

            try {
                setResolvedFieldValue(field, entities.peek(), rawValue);
            } catch (Exception e) {
                System.out.println("Setting `%s` field value for instance of `%s` failed".formatted(field.getName(), entities.peek().getClass().getName()));
            }
        }
    }

    private void attachAttributes(Field field, Attributes attributes) {
        var attributeBinding = field.getDeclaredAnnotation(BindXMLAttribute.class);

        if (attributeBinding != null) {
            var attrValue = attributes.getValue(attributeBinding.alias());

            if (attrValue != null && isAttributeOwnerMatch(attributeBinding)) {
                try {
                    setResolvedFieldValue(field, entities.peek(), attrValue);
                } catch (Exception e) {
                    System.out.println("Setting attribute \"%s\" by owner \"%s\" with `%s` failed.\n%s".formatted(attributeBinding.alias(), attributeBinding.owner(), attrValue, e));
                }
            }
        }
    }

    private M createTargetEntityInstance() {
        try {
            return (M) targetEntityClass.getDeclaredConstructors()[0].newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isAttributeOwnerMatch(BindXMLAttribute binding) {
        return binding.owner().isEmpty() || binding.owner().equals(elements.peek());
    }

    private boolean isTagMatch(Field field, String elementName) {
        var tagBinding = field.getDeclaredAnnotation(BindXMLTag.class);
        return tagBinding != null && tagBinding.alias().equals(elementName);
    }

    private void clear() {
        elements.clear();
        entities.clear();
        fields.clear();
    }

    private void setResolvedFieldValue(Field field, Object target, String value) throws IllegalAccessException {
        field.set(target, FromStringTypes.resolve(field.getType(), value));
    }
}
