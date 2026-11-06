package com.bibliotech.backend.dto.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
        @JsonProperty("nombre")
        String name,

        @Size(max = 50, message = "La identificación no puede tener más de 50 caracteres")
        @JsonProperty("identificacion")
        String identification,

        @Min(value = 1, message = "El tipo de usuario debe ser 1, 2 o 3")
        @Max(value = 3, message = "El tipo de usuario debe ser 1, 2 o 3")
        @JsonProperty("tipoUsuario")
        Integer userType,

        @Size(max = 50, message = "El nombre de usuario no puede tener más de 50 caracteres")
        @JsonProperty("usuario")
        String username,

        @Size(max = 70, message = "La clave no puede tener más de 70 caracteres")
        @JsonProperty("clave")
        String password
) {
}
