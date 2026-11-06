package com.bibliotech.backend.dto.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(max = 50, message = "El nombre de usuario no puede tener más de 50 caracteres")
        @JsonProperty("usuario")
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(max = 255, message = "La contraseña no puede tener más de 255 caracteres")
        @JsonProperty("clave")
        String password
) {
}
