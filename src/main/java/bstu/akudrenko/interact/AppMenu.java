package bstu.akudrenko.interact;

import bstu.akudrenko.storage.DocumentStorage;
import bstu.akudrenko.storage.sql.SqlStorage;
import bstu.akudrenko.storage.xml.XMLStorage;

public class AppMenu extends Menu {
    private final DocumentStorage<?> xmlStorage;
    private final DocumentStorage<?> sqlStorage;
    private final Class cls;

    public AppMenu(Class cls) {
        this.cls = cls;

        var path = "src/main/resources/bstu.akudrenko/handled_public_locations.xml";
        xmlStorage = new XMLStorage(cls, path, "PublicLocationsGroup");
        sqlStorage = new SqlStorage(cls);
    }

    protected void initActions() {
        super.initActions();

        var xmlMenu = new StorageActionsMenu(xmlStorage, cls);
        actions.add(new MenuAction("XML", () -> runMenuPolling(xmlStorage, xmlMenu)));

        var sqlMenu = new StorageActionsMenu(sqlStorage, cls);
        actions.add(new MenuAction("SQL", () -> runMenuPolling(sqlStorage, sqlMenu)));
    }

    private void runMenuPolling(DocumentStorage storage, Menu menu) {
        var searchMenu = new SearchByFieldMenu(storage);
        menu.addAction(new MenuAction("Search by fields", () -> searchMenu.poll(cls)));

        menu.poll();
    }
}
