package com.bibliotech.backend.dto.book.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateBookRequest(
        @NotBlank(message = "El ISBN es obligatorio")
        @Size(max = 50, message = "El ISBN no puede tener más de 50 caracteres")
        @JsonProperty("isbn")
        String isbn,

        @NotBlank(message = "El título es obligatorio")
        @Size(max = 150, message = "El título no puede tener más de 150 caracteres")
        @JsonProperty("titulo")
        String title,

        @NotBlank(message = "El autor es obligatorio")
        @Size(max = 100, message = "El autor no puede tener más de 100 caracteres")
        @JsonProperty("autor")
        String author
) {
}
