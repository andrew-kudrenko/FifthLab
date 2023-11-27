package bstu.akudrenko.storage.xml.bindings;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited()
public @interface BindXMLTag {
    String alias() default "";
}
