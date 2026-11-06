package com.bibliotech.backend.common.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenActionException extends BiblioTechException {
    public ForbiddenActionException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
