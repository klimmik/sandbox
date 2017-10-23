package com.mks.domain.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface XEntity {

    boolean topLevel() default false;

    String url();

    String displayProperty();

}
