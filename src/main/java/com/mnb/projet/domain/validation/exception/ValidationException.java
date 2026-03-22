package com.mnb.projet.domain.validation.exception;

import java.util.Set;

public class ValidationException extends RuntimeException {

    private final Set<ValidationError> erreurs;

    public static final String DONNEES_INCORRECTES_EXCEPTION = "Les données envoyées sont incorrectes";

    public ValidationException(String messageGeneral, Set<ValidationError> erreurs) {
        super(messageGeneral);
        this.erreurs = erreurs;
    }

    public Set<ValidationError> getErreurs() {
        return erreurs;
    }
}
