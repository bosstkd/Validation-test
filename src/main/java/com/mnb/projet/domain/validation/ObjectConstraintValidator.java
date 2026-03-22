package com.mnb.projet.domain.validation;

import com.mnb.projet.domain.validation.rule.ReflectionValidator;

public class ObjectConstraintValidator {

    private ObjectConstraintValidator() {}

    public static <T> void validate(T objectToValidate) {
        ReflectionValidator.validate(objectToValidate);
    }

    /**
     * Validates the object using our annotation-based reflection engine.
     * The {@code group} parameter is kept for API compatibility but is not used —
     * our validation system does not support jakarta-style validation groups.
     */
    public static <T, U> void validate(T objectToValidate, Class<U> group) {
        ReflectionValidator.validate(objectToValidate);
    }
}