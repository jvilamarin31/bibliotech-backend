package com.bibliotech.backend.common.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BiblioTechException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
