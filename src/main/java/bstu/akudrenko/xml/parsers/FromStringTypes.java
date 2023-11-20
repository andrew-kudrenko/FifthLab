package bstu.akudrenko.xml.parsers;

import java.lang.reflect.Type;
import java.util.function.Function;

public class FromStringTypes {
    public static <T> T resolve(Type type, String raw) {
        return (T) getResolver(type).apply(raw);
    }

    public static Function<String, ?> getResolver(Type type) {
        return switch (type.getTypeName()) {
            case "java.lang.String" -> Function.identity();
            case "boolean" -> Boolean::parseBoolean;
            case "int" -> Integer::parseInt;
            case "char" -> v -> v.charAt(0);
            case "double" -> Double::parseDouble;
            default -> throw new IllegalArgumentException("Type `%s` is not supported yet".formatted(type));
        };
    }
}
