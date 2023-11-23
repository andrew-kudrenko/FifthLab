package bstu.akudrenko.interact;

import bstu.akudrenko.models.PublicLocation;
import bstu.akudrenko.storage.xml.XMLStorage;

public class AppMenu extends Menu {
    private SearchByFieldMenu searchMenu = new SearchByFieldMenu(
            new XMLStorage(PublicLocation.class, "src/main/resources/bstu.akudrenko/public_locations.xml"));

    protected void initActions() {
        super.initActions();

        actions.add(new MenuAction("Search by fields", () -> searchMenu.poll(PublicLocation.class)));

        var nested = new Menu(this);

        nested.actions.add(new MenuAction("Print Nested hello!", () -> System.out.println(":)")));
        actions.add(new MenuAction("Open Nested", nested::poll));
    }
}
