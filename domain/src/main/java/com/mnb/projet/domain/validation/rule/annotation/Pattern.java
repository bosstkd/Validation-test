package com.mnb.projet.domain.validation.rule.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * La CharSequence annotée doit correspondre à l'expression régulière indiquée.
 * Les valeurs null sont considérées comme valides (utiliser @NotNull ou @NotEmpty pour les rejeter).
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Pattern {
    String regexp();
    String message() default "n'est pas valide";
    Class<?>[] groups() default {};
}