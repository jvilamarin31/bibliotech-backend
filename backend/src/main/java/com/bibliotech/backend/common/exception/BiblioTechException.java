package com.bibliotech.backend.common.exception;

import org.springframework.http.HttpStatus;

public abstract class BiblioTechException extends RuntimeException{
    private final HttpStatus status;

    protected BiblioTechException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
