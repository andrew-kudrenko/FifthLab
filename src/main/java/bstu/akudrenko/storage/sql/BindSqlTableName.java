package bstu.akudrenko.storage.sql;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited()
public @interface BindSqlTableName {
    String alias() default "";
}
