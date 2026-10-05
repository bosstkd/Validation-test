package com.mnb.projet.domain.validation.rule.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * La méthode annotée doit retourner {@code true}.
 * Elle doit être publique, sans paramètre, et retourner boolean ou Boolean.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssertTrue {
    String message() default "doit être vrai";
    Class<?>[] groups() default {};
}