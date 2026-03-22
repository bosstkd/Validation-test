package com.mnb.projet.domain.validation.rule.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated CharSequence must match the specified regular expression.
 * Null elements are considered valid (use @NotNull or @NotEmpty to reject nulls).
 * Mirrors jakarta.validation.constraints.Pattern behavior.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Pattern {
    String regexp();
    String message() default "n'est pas valide";
}