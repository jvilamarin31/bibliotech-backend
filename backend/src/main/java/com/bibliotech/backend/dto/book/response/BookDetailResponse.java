package com.bibliotech.backend.dto.book.response;

import com.bibliotech.backend.entity.Book;
import com.fasterxml.jackson.annotation.JsonProperty;

public record BookDetailResponse(
        Long id,

        @JsonProperty("isbn")
        String isbn,

        @JsonProperty("titulo")
        String title,

        @JsonProperty("autor")
        String author,

        @JsonProperty("disponible")
        Boolean available
) {
    public static BookDetailResponse fromEntity(Book book) {
        return new BookDetailResponse(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getAvailable()
        );
    }
}
