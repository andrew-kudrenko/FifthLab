package bstu.akudrenko.interact;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private boolean isPolling;
    private Menu parent;
    protected final Scanner input = new Scanner(System.in);
    protected final List<MenuAction> actions = getInitActions();

    public Menu() {}

    public Menu(Menu parent) {
        this.parent = parent;
    }

    public void poll() {
        isPolling = true;

        while (isPolling) {
            show();
        }
    }

    protected List<MenuAction> getInitActions() {
        var actions = new ArrayList<MenuAction>();
        actions.add(new MenuAction("Exit", this::exit));

        return actions;
    }

    protected void printPrelude() {
        System.out.print("> ");
    }

    private void show() {
        for (int i = 0; i < actions.size(); i++) {
            printItem(actions.get(i), i);
        }

        printPrelude();

        var openAt = read();

        if (openAt == -1) {
            exit();
        } else if (openAt >= 0 && openAt < actions.size()) {
            open(openAt);
        }
    }

    protected void exit() {
        isPolling = false;

        if (parent != null) {
            parent.show();
        }
    }

    private int read() {
        try {
            return input.nextInt();
        } catch (Exception e) {
            return -1;
        }
    }

    private void open(int at) {
        var action = actions.get(at);

        if (action != null) {
            action.run();
        }
    }

    private void printItem(MenuAction action, int at) {
        System.out.println(format(action, at));
    }

    private String format(MenuAction action, int at) {
        return "%d. %s".formatted(at, action.getTitle());
    }
}
