package com.mnb.projet.domain.common.exceptions;

public abstract class DomainException extends RuntimeException{

    public DomainException(final String message) {
        super(message);
    }

    public DomainException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DomainException(final Throwable cause) {
        super(cause);
    }
}
