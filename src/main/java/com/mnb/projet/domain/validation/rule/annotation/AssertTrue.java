package com.mnb.projet.domain.validation.rule.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated method must return {@code true}.
 * The method must be public, take no parameters, and return boolean or Boolean.
 * Mirrors jakarta.validation.constraints.AssertTrue behavior.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssertTrue {
    String message() default "doit être vrai";
}