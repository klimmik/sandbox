package com.mks.domain.annotation;

import com.mks.domain.util.validation.ValidationMask;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface XField {

    boolean nullable() default true;

    int length() default 255;

    boolean unique() default false;

    ValidationMask validationMask() default ValidationMask.DEFAULT;

}