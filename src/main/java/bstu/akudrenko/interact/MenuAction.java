package bstu.akudrenko.interact;

import java.util.function.Consumer;
import java.util.function.Function;

public class MenuAction {
    private final String title;
    private final Runnable action;

    public MenuAction(String title, Runnable action) {
        this.title = title;
        this.action = action;
    }

    public String getTitle() {
        return title;
    }
    public void run() {
        action.run();
    }
}
