package fr.nimrod.info.test.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface DataExpected {
	String file();
	String tableName();
	String orderBy();
	String[] ignoredColumn() default {};
}
