package bstu.akudrenko.storage.xml.bindings;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited()
public @interface BindXMLAttribute {
    String alias() default "";
    String owner() default "";
}
