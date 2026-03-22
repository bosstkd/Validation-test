package com.mnb.projet.domain.common.exceptions;

public abstract class MnbException extends RuntimeException{

    public MnbException(final String message) {
        super(message);
    }

    public MnbException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MnbException(final Throwable cause) {
        super(cause);
    }
}
