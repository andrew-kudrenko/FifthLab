package bstu.akudrenko.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ClassReflectionUtils {
    private static final List<Class> primitiveClasses = getWrapperTypes();

    public static boolean isPrimitive(Class cls) {
        return primitiveClasses.contains(cls);
    }

    private static List<Class> getWrapperTypes(){
        var types = new ArrayList<Class>();

        types.add(Boolean.class);
        types.add(Character.class);
        types.add(Byte.class);
        types.add(Short.class);
        types.add(Integer.class);
        types.add(Long.class);
        types.add(Float.class);
        types.add(Double.class);
        types.add(Void.class);

        return types;
    }

    public static <M> Tuple<Field, Object> deepGetField(M model, String path) {
        var parts = path.split("\\.");

        try {
            Field field = model.getClass().getField(parts[0]);
            Object value = field.get(model);

            for (var i = 1; i < parts.length; i++) {
                field = value.getClass().getField(parts[i]);
                value = field.get(value);
            }

            return Tuple.of(field, value);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
