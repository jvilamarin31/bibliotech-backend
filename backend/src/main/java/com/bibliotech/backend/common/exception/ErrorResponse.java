package com.bibliotech.backend.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String mensaje,
        String path,
        List<String> detalles
) {
    public ErrorResponse(int status, String mensaje, String path) {
        this(LocalDateTime.now(), status, mensaje, path, null);
    }

    public ErrorResponse(int status, String mensaje, String path, List<String> detalles) {
        this(LocalDateTime.now(), status, mensaje, path, detalles);
    }
}
