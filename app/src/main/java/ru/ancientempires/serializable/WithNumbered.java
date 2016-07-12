package ru.ancientempires.serializable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// У этой аннотации две функции
@Retention(RetentionPolicy.RUNTIME)
public @interface WithNumbered
{
	String value();

	boolean checkGameForNull() default false;
}
