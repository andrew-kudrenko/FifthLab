package bstu.akudrenko.xml.bindings;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited()
public @interface BindXMLEntity {
    String alias() default "";
}
