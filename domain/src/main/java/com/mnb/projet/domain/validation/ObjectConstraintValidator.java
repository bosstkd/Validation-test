package com.mnb.projet.domain.validation;

import com.mnb.projet.domain.validation.rule.ReflectionValidator;

public class ObjectConstraintValidator {

    private ObjectConstraintValidator() {}

    /**
     * Valide l'objet avec notre moteur de réflexion basé sur les annotations.
     * Seules les contraintes appartenant à l'un des {@code groups} fournis sont vérifiées ;
     * sans groupe, c'est le groupe {@code Default} qui est validé.
     */
    public static <T> void validate(T objectToValidate, Class<?>... groups) {
        ReflectionValidator.validate(objectToValidate, groups);
    }
}
