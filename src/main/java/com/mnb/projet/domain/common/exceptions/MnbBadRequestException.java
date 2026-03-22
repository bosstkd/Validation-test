package com.mnb.projet.domain.common.exceptions;

import lombok.Getter;

import java.util.Set;

public class MnbBadRequestException extends MnbException {
    @Getter
    private Set<String> details;
    public static final String TEST_ERROR_BadRequest_MESSAGE = "Hello Bad request exception";

    public MnbBadRequestException(final String message) {
        super(message);
    }

    public MnbBadRequestException(final String message, Set<String> details) {
        super(message);
        this.details = details;
    }
}
