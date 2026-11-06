package com.bibliotech.backend.dto.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record CreateUserRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
        @JsonProperty("nombre")
        String name,

        @NotBlank(message = "La identificación es obligatoria")
        @Size(max = 50, message = "La identificación no puede tener más de 50 caracteres")
        @JsonProperty("identificacion")
        String identification,

        @NotNull(message = "El tipo de usuario es obligatorio")
        @Min(value = 1, message = "El tipo de usuario debe ser 1 (Afiliado), 2 (Empleado) o 3 (Invitado)")
        @Max(value = 3, message = "El tipo de usuario debe ser 1 (Afiliado), 2 (Empleado) o 3 (Invitado)")
        @JsonProperty("tipoUsuario")
        Integer userType,

        @NotBlank(message = "El usuario es obligatorio")
        @Size(max = 50, message = "El nombre de usuario no puede tener más de 50 caracteres")
        @JsonProperty("usuario")
        String username,

        @NotBlank(message = "La clave es obligatoria")
        @Size(max = 70, message = "La clave no puede tener más de 70 caracteres")
        @JsonProperty("clave")
        String password
) {
}
