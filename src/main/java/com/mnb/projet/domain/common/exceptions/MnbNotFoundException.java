package com.mnb.projet.domain.common.exceptions;

import lombok.Getter;

import java.util.Set;

public class MnbNotFoundException extends MnbException {

    @Getter
    private Set<String> details;
    public static final String TEST_ERROR_NotFound_MESSAGE = "Hello Not Found exception";

    public MnbNotFoundException(final String message) {
        super(message);
    }

    public MnbNotFoundException(final String message, Set<String> details) {
        super(message);
        this.details = details;
    }
}
