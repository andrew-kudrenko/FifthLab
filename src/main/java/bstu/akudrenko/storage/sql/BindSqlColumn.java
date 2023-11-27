package bstu.akudrenko.storage.sql;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited()
public @interface BindSqlColumn {
    String alias() default "";
}
