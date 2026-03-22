package com.mnb.projet.domain.validation.rule.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated field must not be null and must contain at least one non-whitespace character.
 * Mirrors jakarta.validation.constraints.NotBlank behavior.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlank {
    String message() default "ne peut pas être vide ou blanc";
}