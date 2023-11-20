package bstu.akudrenko;

import bstu.akudrenko.interact.AppMenu;

public class Main {
    public static void main(String[] args) {
        var menu = new AppMenu();

        menu.poll();
    }
}