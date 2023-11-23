package bstu.akudrenko.interact;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private boolean isPolling;
    private Menu parent;
    protected String delimiter = "_".repeat(20);
    protected final Scanner input = new Scanner(System.in);
    protected final List<MenuAction> actions = new ArrayList<>();

    public Menu() {
        initActions();
    }

    public Menu(Menu parent) {
        initActions();
        this.parent = parent;
    }

    public void poll() {
        isPolling = true;

        while (isPolling) {
            show();
        }
    }

    protected void initActions() {
        actions.clear();
        actions.add(new MenuAction("Exit", this::exit));
    }

    protected void printPrelude() {
        System.out.print("> ");
    }

    private void show() {
        printDelimiter();
        for (int i = 0; i < actions.size(); i++) {
            printItem(actions.get(i), i);
        }
        printDelimiter();
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

    protected void printItem(MenuAction action, int at) {
        System.out.println(format(action, at));
    }

    protected void printDelimiter() {
        System.out.println(delimiter);
    }

    private String format(MenuAction action, int at) {
        return "%d. %s".formatted(at, action.getTitle());
    }
}
