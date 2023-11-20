package bstu.akudrenko.output;

import java.lang.reflect.Field;

public class ModelPrinter {
    public void println(Object model) {
        println(model, 0);
    }

    private void println(Object model, int depth) {
        if (depth == 0) {
            System.out.println(model.getClass().getTypeName());
            println(model, depth + 1);
        } else {
            var stringify = model.getClass().getDeclaredAnnotation(Stringify.class);

            if (stringify != null) {
                try {
                    for (var field : model.getClass().getDeclaredFields()) {
                        var value = field.get(model);

                        if (value.getClass().isAnnotationPresent(Stringify.class)) {
                            System.out.println(getIndent(stringify, depth) + field.getName());
                            println(value, depth + 1);
                        } else {
                            System.out.print(stringifyField(stringify, field, value, depth));
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println(getIndent(stringify, depth) + model);
            }
        }

    }

    private String stringifyField(Stringify annotation, Field field, Object value, int depth) {
        return getIndent(annotation, depth) +
                annotation.startOfLine() + field.getName() +
                annotation.keyValueDelimiter() + '"' + value + '"' +
                annotation.endOfLine();
    }

    private String getIndent(Stringify annotation, int depth) {
        return " ".repeat(depth * annotation.indent());
    }
}
