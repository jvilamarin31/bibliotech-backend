package com.bibliotech.backend.dto.book.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;

public record UpdateBookRequest(
        @Size(max = 50, message = "El ISBN no puede tener más de 50 caracteres")
        @JsonProperty("isbn")
        String isbn,

        @Size(max = 150, message = "El título no puede tener más de 150 caracteres")
        @JsonProperty("titulo")
        String title,

        @Size(max = 100, message = "El autor no puede tener más de 100 caracteres")
        @JsonProperty("autor")
        String author,

        @JsonProperty("disponible")
        Boolean available
) {
}
