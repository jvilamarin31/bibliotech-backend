package com.bibliotech.backend.common.exception;

import org.springframework.http.HttpStatus;

public class BusinessRuleException extends BiblioTechException {
    public BusinessRuleException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
