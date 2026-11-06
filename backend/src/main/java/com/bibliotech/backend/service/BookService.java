package com.bibliotech.backend.service;

import com.bibliotech.backend.common.exception.DuplicateResourceException;
import com.bibliotech.backend.common.exception.ResourceNotFoundException;
import com.bibliotech.backend.dto.book.request.CreateBookRequest;
import com.bibliotech.backend.dto.book.request.UpdateBookRequest;
import com.bibliotech.backend.dto.book.response.BookDetailResponse;
import com.bibliotech.backend.entity.Book;
import com.bibliotech.backend.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookDetailResponse createBook(CreateBookRequest request) {
        if (bookRepository.existsByIsbn(request.isbn())) {
            throw new DuplicateResourceException("El ISBN ya está registrado");
        }

        Book book = Book.builder()
                .isbn(request.isbn())
                .title(request.title())
                .author(request.author())
                .available(true)
                .build();

        return BookDetailResponse.fromEntity(bookRepository.save(book));
    }

    public List<BookDetailResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDetailResponse::fromEntity)
                .toList();
    }

    public BookDetailResponse getBookByIsbn(String isbn) {
        Optional<Book> bookByIsbn = bookRepository.findByIsbn(isbn);
        if (bookByIsbn.isEmpty()) {
            throw new ResourceNotFoundException("El libro con ISBN: "+ isbn +" no existe. ");
        }

        return BookDetailResponse.fromEntity(bookByIsbn.get());
    }

    public BookDetailResponse updateBook(String isbn, UpdateBookRequest request) {
        Optional<Book> bookByIsbn = bookRepository.findByIsbn(isbn);
        if (bookByIsbn.isEmpty()) {
            throw new ResourceNotFoundException("El libro con ISBN: "+ isbn +" no existe. ");
        }
        Book bookExists = bookByIsbn.get();
        if (request.isbn() != null && !bookExists.getIsbn().equals(request.isbn())) {
            if (bookRepository.existsByIsbn(request.isbn())) {
                throw new DuplicateResourceException("El nuevo ISBN ya está en uso por otro libro.");
            }
            bookExists.setIsbn(request.isbn());
        }
        if (request.title() != null) bookExists.setTitle(request.title());
        if (request.author() != null) bookExists.setAuthor(request.author());
        if (request.available() != null) bookExists.setAvailable(request.available());

        Book savedBook = bookRepository.save(bookExists);
        return BookDetailResponse.fromEntity(savedBook);
    }

    public void deleteBook(String isbn) {
        Optional<Book> bookByIsbn = bookRepository.findByIsbn(isbn);
        if (bookByIsbn.isEmpty()) {
            throw new ResourceNotFoundException("El libro con ISBN: "+ isbn +" no existe. ");
        }

        bookRepository.delete(bookByIsbn.get());
    }
}
