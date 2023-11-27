package bstu.akudrenko.interact;

import bstu.akudrenko.models.Model;
import bstu.akudrenko.storage.DocumentStorage;

public class StorageActionsMenu<M extends Model> extends FieldsMenu {
    private final DocumentStorage<M> storage;
    private final SearchByFieldMenu searchMenu;

    public StorageActionsMenu(DocumentStorage<M> storage, Class<M> cls) {
        super();

        this.storage = storage;
        this.cls = cls;
        try {
            idField = cls.getField("id");
        } catch (Exception e) {
            System.out.println(e);
        }
        searchMenu = new SearchByFieldMenu(storage, this);
    }

    @Override
    protected void initActions() {
        super.initActions();

        actions.add(new MenuAction("Get all", this::printAll));
        actions.add(new MenuAction("Remove by id", this::removeById));
        actions.add(new MenuAction("Update", this::update));
        actions.add(new MenuAction("Add", this::add));
        actions.add(new MenuAction("Search by field", () -> searchMenu.poll(cls)));
    }

    private void update() {
        var id = readId();
        var found = storage.where(m -> isFieldValueEquals(idField, m, id)).stream().findFirst();

        found.ifPresent(m -> {
            var menu = new UpdateFieldsMenu(storage, m);
            menu.poll(cls);
        });
    }

    private void add() {
        new UpdateFieldsMenu(storage, null).poll(cls);
    }

    private void removeById() {
        try {
            storage.remove(readId());
            printAll();
        } catch (Exception e) {
            System.out.println("Removing by id failed. " + e);
        }

    }

    private int readId() {
        return getFieldValue(idField);
    }

    private void printAll() {
        storage.getAll().forEach(printer::println);
    }
}
