package fr.nimrod.info.test.annotations;

import java.lang.annotation.Repeatable;

@Repeatable(Datas.class)
public @interface Data {
	
	String value();
	Phase phase() default Phase.BEFORE;
	
	public enum Phase {
		BEFORE,
		AFTER;
	}
}