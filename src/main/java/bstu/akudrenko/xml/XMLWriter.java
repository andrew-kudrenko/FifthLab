package bstu.akudrenko.xml;

import bstu.akudrenko.models.Model;
import bstu.akudrenko.xml.bindings.BindXMLAttribute;
import bstu.akudrenko.xml.bindings.BindXMLEntity;
import bstu.akudrenko.xml.bindings.BindXMLNestedEntity;
import bstu.akudrenko.xml.bindings.BindXMLTag;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class XMLWriter {
    private final Transformer transformer;
    private final DocumentBuilder builder;
    private final String rootElementName;
    private Document document;

    private final File destination;

    public XMLWriter(File destination, String rootElementName) {
        this.destination = destination;
        this.rootElementName = rootElementName;

        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            transformer = TransformerFactory.newInstance().newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        } catch (Exception e) {
            throw new RuntimeException("Creating XML writer failed. " + e);
        }
    }

    public <M extends Model> void write(List<M> models) {
        document = builder.newDocument();
        var root = document.createElement(rootElementName);
        document.appendChild(root);

        models.forEach(m -> root.appendChild(elementFromModel(m, null)));

        var dom = new DOMSource(document);
        var file = new StreamResult(destination);

        try {
            transformer.transform(dom, file);
        } catch (Exception e) {
            System.out.println("Writing failed. " + e);
            throw new RuntimeException(e);
        }
    }

    private <M extends Model> Element elementFromModel(M model, BindXMLNestedEntity nestedBinding) {
        var root = document.createElement(getModelElementName(model, nestedBinding));
        var fields = model.getClass().getDeclaredFields();

        filterByBinding(fields, BindXMLTag.class)
            .forEach(f -> {
                var binding = f.getDeclaredAnnotation(BindXMLTag.class);
                var element = document.createElement(binding.alias());

                try {
                    element.setTextContent(f.get(model).toString());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                root.appendChild(element);
            });

        filterByBinding(fields, BindXMLAttribute.class)
            .forEach(f -> {
                var binding = f.getDeclaredAnnotation(BindXMLAttribute.class);
                var owner = getAttributeOwner(root, binding);

                if (owner != null) {
                    try {
                        owner.setAttribute(binding.alias(), f.get(model).toString());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        filterByBinding(fields, BindXMLNestedEntity.class)
            .forEach(f -> {
                try {
                    root.appendChild(elementFromModel((M) f.get(model), f.getDeclaredAnnotation(BindXMLNestedEntity.class)));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

        return root;
    }

    private Element getAttributeOwner(Element root, BindXMLAttribute binding) {
        if (binding.owner().isEmpty()) {
            return root;
        }

        var nodes = root.getChildNodes();

        for (var i = 0; i < nodes.getLength(); i++) {
            var node = nodes.item(i);

            if (node.getNodeName().equals(binding.owner())) {
                return (Element)node;
            }
        }

        return null;
    }

    private <T extends Annotation> Stream<Field> filterByBinding(Field[] fields, Class<T> cls) {
        return Arrays.stream(fields).filter(f -> f.isAnnotationPresent(cls));
    }

    private String getModelElementName(Model model, BindXMLNestedEntity nestedBinding) {
        if (nestedBinding != null && !nestedBinding.alias().isEmpty()) {
            return nestedBinding.alias();
        }

        return model.getClass().getDeclaredAnnotation(BindXMLEntity.class).alias();
    }
}
