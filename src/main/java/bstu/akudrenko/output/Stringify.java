package bstu.akudrenko.output;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited()
public @interface Stringify {
    String startOfLine() default "";
    String keyValueDelimiter() default ": ";
    String endOfLine() default "\n";
    int indent() default 2;
}

