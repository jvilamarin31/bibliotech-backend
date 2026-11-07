package com.bibliotech.backend.dto.loan.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateLoanRequest(
        @NotBlank(message = "El ISBN es obligatorio")
        @JsonProperty("isbn")
        String isbn,

        @NotBlank(message = "La identificación del usuario es obligatoria")
        @JsonProperty("identificacionUsuario")
        String userIdentification,

        @NotNull(message = "El tipo de usuario es obligatorio")
        @Min(value = 1, message = "El tipo de usuario debe ser 1, 2 o 3")
        @Max(value = 3, message = "El tipo de usuario debe ser 1, 2 o 3")
        @JsonProperty("tipoUsuario")
        Integer userType
) {
}
