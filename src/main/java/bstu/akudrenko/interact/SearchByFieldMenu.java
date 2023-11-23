package bstu.akudrenko.interact;

import bstu.akudrenko.output.ModelPrinter;
import bstu.akudrenko.storage.DocumentStorage;
import bstu.akudrenko.storage.ModelSearch;
import bstu.akudrenko.utils.Triple;
import bstu.akudrenko.utils.Tuple;
import bstu.akudrenko.xml.bindings.BindXMLAttribute;
import bstu.akudrenko.xml.bindings.BindXMLNestedEntity;
import bstu.akudrenko.xml.bindings.BindXMLTag;
import bstu.akudrenko.xml.parsers.FromStringTypes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchByFieldMenu extends Menu {
    private final ModelPrinter printer = new ModelPrinter();
    private final ModelSearch search;
    private HashMap<Field, Tuple<String, Object>> fields = new HashMap<>();
    private Class<?> cls;

    public SearchByFieldMenu(DocumentStorage storage) {
        super();
        search = new ModelSearch(storage);
    }

    public void poll(Class<?> cls) {
        this.cls = cls;
        updateActions();
        fields = new HashMap<>();

        super.poll();
    }

    protected void updateActions() {
        super.initActions();
        actions.add(new MenuAction("Search", () -> search.search(fields).forEach(printer::println)));

        for (var entry : getFields(cls, Tuple.of("", ""))) {
            var field = entry.getThird();
            var title = "\"%s\" (%s)".formatted(entry.getSecond(), field.getType().getName());

            actions.add(new MenuAction(title, () -> addField(field, entry.getFirst())));
        }
    }

    private List<Triple<String, String, Field>> getFields(Class cls, Tuple<String, String> prefix) {
        var fields = new ArrayList<Triple<String, String, Field>>();

        for (var field : cls.getDeclaredFields()) {
            var fieldNames = getFieldDisplayName(field);

            if (!field.isAnnotationPresent(BindXMLNestedEntity.class)) {
                fields.add(Triple.of(
                        prefix.getFirst() + fieldNames.getFirst(),
                        prefix.getSecond() + fieldNames.getSecond(),
                        field
                ));
            } else {
                fields.addAll(getFields(field.getType(), Tuple.of(
                        prefix.getFirst() + fieldNames.getFirst() + '.',
                        prefix.getSecond() + fieldNames.getSecond() + '.')
                ));
            }
        }

        return fields;
    }

    private void addField(Field field, String path) {
        try {
            fields.put(field, Tuple.of(path, getFieldValue(field)));
        } catch (Exception e) {
            System.out.println("Incorrect input type. " + e);
        }
    }

    private Tuple<String, String> getFieldDisplayName(Field field) {
        var tagBinding = field.getDeclaredAnnotation(BindXMLTag.class);
        if (tagBinding != null) {
            return Tuple.of(field.getName(), tagBinding.alias());
        }

        var attributeBinding = field.getDeclaredAnnotation(BindXMLAttribute.class);
        if (attributeBinding != null) {
            return Tuple.of(field.getName(), attributeBinding.alias());
        }

        var nestedEntityBinding = field.getDeclaredAnnotation(BindXMLNestedEntity.class);
        if (nestedEntityBinding != null) {
            return Tuple.of(field.getName(), nestedEntityBinding.alias());
        }

        return Tuple.of(field.getName(), field.getName());
    }

    private <T> T getFieldValue(Field field) {
        return FromStringTypes.resolve(field.getType(), promptFieldValue(field));
    }

    private String promptFieldValue(Field field) {
        System.out.print("%s (%s) > ".formatted(getFieldDisplayName(field).getSecond(), field.getType().getName()));
        return input.next();
    }
}
