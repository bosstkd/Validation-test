package com.mnb.projet.domain.common.exceptions;

public class DomainInternalException extends DomainException {

    public static final String TEST_ERROR_Server_MESSAGE = "Hello server exception";
    public static final String DEFAULT_ERROR = "Le service est indisponible pour l'instant, veuillez réessayer ultérieurement.";

    public DomainInternalException(final String message) {
        super(message);
    }
}
