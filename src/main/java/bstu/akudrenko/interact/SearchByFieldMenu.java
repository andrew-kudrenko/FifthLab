package bstu.akudrenko.interact;

import bstu.akudrenko.output.ModelPrinter;
import bstu.akudrenko.storage.DocumentStorage;
import bstu.akudrenko.storage.ModelSearch;
import bstu.akudrenko.xml.bindings.BindXMLNestedEntity;
import bstu.akudrenko.xml.parsers.FromStringTypes;

import java.lang.reflect.Field;
import java.util.HashMap;

public class SearchByFieldMenu extends Menu {
    private final ModelPrinter printer = new ModelPrinter();
    private final ModelSearch search;
    private HashMap<Field, Object> fields = new HashMap<>();
    private Class<?> cls;

    public SearchByFieldMenu(DocumentStorage storage) {
        search = new ModelSearch(storage);
    }

    public void poll(Class<?> cls) {
        this.cls = cls;
        initActions();
        fields = new HashMap<>();

        super.poll();
    }

    protected void initActions() {
        actions.clear();
        actions.add(new MenuAction("Exit", this::exit));

        for (var field : cls.getDeclaredFields()) {
            if (!field.isAnnotationPresent(BindXMLNestedEntity.class)) {
                var title = "\"%s\" (%s)".formatted(field.getName(), field.getType().getName());
                actions.add(new MenuAction(title, () -> addField(field)));
            }
        }

        actions.add(new MenuAction("Find!", () -> search.search(fields).forEach(printer::println)));
    }

    private void addField(Field field) {
        try {
            fields.put(field, getFieldValue(field));
        } catch (Exception e) {
            System.out.println("Incorrect input type. " + e);
        }
    }

    private <T> T getFieldValue(Field field) {
        return FromStringTypes.resolve(field.getType(), promptFieldValue(field));
    }

    private String promptFieldValue(Field field) {
        System.out.print("%s (%s) > ".formatted(field.getName(), field.getType().getName()));
        return input.next();
    }
}
