package com.mnb.projet.domain.validation.rule.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated field must not be null and must not be empty.
 * Applies to String, Collection, Map, and array types.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmpty {
    String message() default "ne peut pas être vide";
}