package bstu.akudrenko.interact;

import bstu.akudrenko.storage.DocumentStorage;
import bstu.akudrenko.storage.ModelSearch;

public class SearchByFieldMenu extends FieldsMenu {
    private final ModelSearch search;

    public SearchByFieldMenu(DocumentStorage storage) {
        super();
        search = new ModelSearch(storage);
    }



    @Override
    protected void initActions() {
        super.initActions();

        addExecutableActions();
        addActionsByFields();
    }

    public SearchByFieldMenu(DocumentStorage storage, Menu parent) {
        super(parent);
        search = new ModelSearch(storage);
    }

    @Override
    protected void addExecutableActions() {
        actions.add(new MenuAction("Search", () -> search.search(fields).forEach(printer::println)));
    }
}
