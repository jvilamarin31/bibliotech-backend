package com.bibliotech.backend.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends BiblioTechException {
    public InvalidCredentialsException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
