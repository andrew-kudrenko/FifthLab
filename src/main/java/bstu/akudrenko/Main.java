package bstu.akudrenko;

import bstu.akudrenko.interact.AppMenu;
import bstu.akudrenko.models.PublicLocation;

public class Main {
    public static void main(String[] args) {
        var appMenu = new AppMenu(PublicLocation.class);
        appMenu.poll();
    }
}