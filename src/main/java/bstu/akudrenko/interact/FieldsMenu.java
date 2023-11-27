package bstu.akudrenko.interact;

import bstu.akudrenko.output.ModelPrinter;
import bstu.akudrenko.utils.Triple;
import bstu.akudrenko.utils.Tuple;
import bstu.akudrenko.storage.xml.bindings.BindXMLAttribute;
import bstu.akudrenko.storage.xml.bindings.BindXMLNestedEntity;
import bstu.akudrenko.storage.xml.bindings.BindXMLTag;
import bstu.akudrenko.storage.xml.parsers.FromStringTypes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public abstract class FieldsMenu extends Menu {
    protected final Scanner input = new Scanner(System.in);
    protected final ModelPrinter printer = new ModelPrinter();
    protected HashMap<Field, Tuple<String, Object>> fields = new HashMap<>();

    protected Class<?> cls;
    protected Field idField;

    public FieldsMenu() {
        super();
    }

    public FieldsMenu(Menu parent) {
        super(parent);
    }

    public void poll(Class<?> cls) {
        this.cls = cls;

        try {
            idField = cls.getField("id");
        } catch (Exception e) {
            System.out.println(e);
        }

        fields = new HashMap<>();

        addExecutableActions();
        addActionsByFields();

        super.poll();
    }

    protected void addExecutableActions() {}

    protected void addActionsByFields() {
        for (var entry : getFields(cls, Tuple.of("", ""))) {
            var field = entry.getThird();
            var title = "\"%s\" (%s)".formatted(entry.getSecond(), field.getType().getName());

            actions.add(new MenuAction(title, () -> addField(field, entry.getFirst())));
        }
    }

    protected List<Triple<String, String, Field>> getFields(Class cls, Tuple<String, String> prefix) {
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

    protected void addField(Field field, String path) {
        try {
            fields.put(field, Tuple.of(path, getFieldValue(field)));
        } catch (Exception e) {
            System.out.println("Incorrect input type. " + e);
        }
    }

    protected Tuple<String, String> getFieldDisplayName(Field field) {
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

    protected <T> T getFieldValue(Field field) {
        return FromStringTypes.resolve(field.getType(), promptFieldValue(field));
    }

    protected String promptFieldValue(Field field) {
        System.out.print("%s (%s) > ".formatted(getFieldDisplayName(field).getSecond(), field.getType().getName()));
        return input.nextLine();
    }

    protected <V> boolean isFieldValueEquals(Field field, Object owner, V value) {
        try {
            return field.get(owner).equals(value);
        } catch (Exception e) {
            return false;
        }
    }
}
