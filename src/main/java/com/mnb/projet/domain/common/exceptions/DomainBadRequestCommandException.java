package com.mnb.projet.domain.common.exceptions;

import lombok.Getter;

import java.util.Set;

public class DomainBadRequestCommandException extends DomainException {
    @Getter
    private Set<String> details;
    public static final String TEST_ERROR_BadRequest_MESSAGE = "Hello Bad request exception";

    public DomainBadRequestCommandException(final String message) {
        super(message);
    }

    public DomainBadRequestCommandException(final String message, Set<String> details) {
        super(message);
        this.details = details;
    }
}
