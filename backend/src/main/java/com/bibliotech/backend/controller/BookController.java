package com.bibliotech.backend.controller;

import com.bibliotech.backend.dto.book.request.CreateBookRequest;
import com.bibliotech.backend.dto.book.request.UpdateBookRequest;
import com.bibliotech.backend.dto.book.response.BookDetailResponse;
import com.bibliotech.backend.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/libros")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookDetailResponse> createBook(@Valid @RequestBody CreateBookRequest request) {
        BookDetailResponse response = bookService.createBook(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookDetailResponse>> getAllBooks() {
        List<BookDetailResponse> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<BookDetailResponse> getBook(@PathVariable String isbn) {
        return ResponseEntity.ok(bookService.getBookByIsbn(isbn));
    }

    @PatchMapping("/{isbn}")
    public ResponseEntity<BookDetailResponse> updateBook(@PathVariable String isbn, @Valid @RequestBody UpdateBookRequest request) {
        return ResponseEntity.ok(bookService.updateBook(isbn, request));
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> deleteBook(@PathVariable String isbn) {
        bookService.deleteBook(isbn);
        return ResponseEntity.noContent().build();
    }


}
