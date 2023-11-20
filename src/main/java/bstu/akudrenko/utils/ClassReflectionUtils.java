package bstu.akudrenko.utils;

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
}
