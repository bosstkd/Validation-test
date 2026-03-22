package com.mnb.projet.domain.common.exceptions;

import lombok.Getter;

import java.util.Set;

public class DomainResourceNotFoundException extends DomainException {

    @Getter
    private Set<String> details;
    public static final String TEST_ERROR_NotFound_MESSAGE = "Hello Not Found exception";

    public DomainResourceNotFoundException(final String message) {
        super(message);
    }

    public DomainResourceNotFoundException(final String message, Set<String> details) {
        super(message);
        this.details = details;
    }
}
