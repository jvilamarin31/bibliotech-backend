package com.bibliotech.backend.dto.user.response;

import com.bibliotech.backend.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UserDetailResponse(

        Long id,

        @JsonProperty("nombre")
        String name,

        @JsonProperty("identificacion")
        String identification,

        @JsonProperty("tipoUsuario")
        Integer userType,

        @JsonProperty("usuario")
        String username

) {
    public static UserDetailResponse fromEntity(User user) {
        return new UserDetailResponse(
                user.getId(),
                user.getName(),
                user.getIdentification(),
                user.getUserType(),
                user.getUsername()
        );
    }
}
