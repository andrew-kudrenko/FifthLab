package bstu.akudrenko.interact;

import bstu.akudrenko.models.PublicLocation;
import bstu.akudrenko.storage.xml.XMLStorage;
import bstu.akudrenko.xml.TestXML;

public class AppMenu extends Menu {
    private TestXML xml = new TestXML();
    private SearchByFieldMenu searchMenu = new SearchByFieldMenu(new XMLStorage(PublicLocation.class));

    public AppMenu() {
        super();
        initActions();
    }

    private void initActions() {
        actions.add(new MenuAction("Get All", () -> xml.getAll()));
        actions.add(new MenuAction("Search by fields", () -> searchMenu.poll(PublicLocation.class)));

        var nested = new Menu(this);

        nested.actions.add(new MenuAction("Print Nested hello!", () -> System.out.println(":)")));
        actions.add(new MenuAction("Open Nested", nested::poll));
    }
}
