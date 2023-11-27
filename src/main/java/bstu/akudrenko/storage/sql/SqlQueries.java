package bstu.akudrenko.storage.sql;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class SqlQueries {
    public static String getColumnDeclaration(Field field) {
        return "%s %s".formatted(getColumnName(field), getSqlTypeName(field.getType()));
    }

    public static String escapeValue(Object value) {
        return switch (value) {
            case String s -> "\"" + s + "\"";
            default -> value.toString();
        };
    }

    public static String getColumnName(Field field) {
        var binding = field.getDeclaredAnnotation(BindSqlColumn.class);

        if (binding != null && !binding.alias().isEmpty()) {
            return binding.alias();
        }

        return field.getName();
    }

    private static String getSqlTypeName(Type type) {
        return switch(type.getTypeName()) {
            case "java.lang.String" -> "varchar(255)";
            case "char" -> "char(1)";
            case "int" -> "int";
            case "double" -> "double";
            case "boolean" -> "boolean";
            default -> throw new IllegalArgumentException("Type `%s` is not supported yet".formatted(type));
        };
    }
}
