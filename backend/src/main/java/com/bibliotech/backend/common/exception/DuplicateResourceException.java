package com.bibliotech.backend.common.exception;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends BiblioTechException {
    public DuplicateResourceException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
