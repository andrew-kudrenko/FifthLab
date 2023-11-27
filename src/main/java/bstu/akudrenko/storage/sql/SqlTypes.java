package bstu.akudrenko.storage.sql;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.function.Function;

public class SqlTypes {
    public static Object get(ResultSet set, Field field) {
        return getResolver(set, field).apply(SqlQueries.getColumnName(field));
    }

    private static Function<String, ?> getResolver(ResultSet set, Field field) {
        return switch (field.getType().getTypeName()) {
            case "java.lang.String" -> v -> {
                try {
                    return set.getString(v);
                } catch (Exception e) {
                    return "";
                }
            };
            case "boolean" -> v -> {
                try {
                    return set.getBoolean(v);
                } catch (Exception e) {
                    return false;
                }
            };
            case "int" -> v -> {
                try {
                    return set.getInt(v);
                } catch (Exception e) {
                    return 0;
                }
            };
            case "char" -> v -> {
                try {
                    var result = set.getString(v);
                    return result.length() > 0 ? result.charAt(0) : ' ';
                } catch (Exception e) {
                    return ' ';
                }
            };
            case "double" -> v -> {
                try {
                    return set.getDouble(v);
                } catch (Exception e) {
                    return 0;
                }
            };
            default -> throw new IllegalArgumentException("Type is not supported yet");
        };
    }
}
