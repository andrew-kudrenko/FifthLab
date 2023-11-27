package bstu.akudrenko.interact;

import bstu.akudrenko.models.Model;
import bstu.akudrenko.storage.DocumentStorage;

public class UpdateFieldsMenu<M extends Model> extends FieldsMenu {
    private final DocumentStorage<M> storage;
    private M model;
    @Override
    protected void initActions() {
        super.initActions();

        addExecutableActions();
        addActionsByFields();
    }

    public UpdateFieldsMenu(DocumentStorage storage, M model) {
        super();
        this.model = model;
        this.storage = storage;
    }

    @Override
    protected void addExecutableActions() {
        actions.add(new MenuAction("Save", model != null ? this::update : this::add));
    }

    private void update() {
        try {
            var id = (int) idField.get(model);
            var found = storage.getById(id);

            found.ifPresent(m -> {
                try {
                    idField.set(m, id);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                fields.forEach((f, v) -> {
                    try {
                        f.set(m, v.getSecond());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

                try {
                    for (var f : m.getClass().getDeclaredFields()) {
                        System.out.println(f + ": " + f.get(m));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                storage.update(m);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            fields.clear();
        }
    }

    private void add() {
        try {
            var newModel = (M) cls.getDeclaredConstructors()[0].newInstance();

            fields.forEach((f, v) -> {
                try {
                    f.set(newModel, v.getSecond());
                } catch (Exception e) {
                    System.out.println(e);
                }
            });

            storage.add(newModel);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fields.clear();
        }
    }
}
