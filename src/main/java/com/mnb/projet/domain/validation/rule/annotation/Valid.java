package com.mnb.projet.domain.validation.rule.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field for cascaded validation: the ReflectionValidator will recursively
 * validate the nested object using its own annotations.
 * Mirrors jakarta.validation.Valid behavior.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Valid {
}