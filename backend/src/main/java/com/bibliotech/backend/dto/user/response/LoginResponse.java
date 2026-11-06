package com.bibliotech.backend.dto.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponse(
        String token,

        @JsonProperty("usuario")
        String username,

        @JsonProperty("nombre")
        String name,

        @JsonProperty("tipoUsuario")
        Integer userType
) {
}
