package com.bibliotech.backend.service;

import com.bibliotech.backend.common.exception.BusinessRuleException;
import com.bibliotech.backend.common.exception.ForbiddenActionException;
import com.bibliotech.backend.common.exception.ResourceNotFoundException;
import com.bibliotech.backend.dto.loan.request.CreateLoanRequest;
import com.bibliotech.backend.dto.loan.response.LoanDetailResponse;
import com.bibliotech.backend.entity.Book;
import com.bibliotech.backend.entity.Loan;
import com.bibliotech.backend.entity.User;
import com.bibliotech.backend.repository.BookRepository;
import com.bibliotech.backend.repository.LoanRepository;
import com.bibliotech.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public LoanDetailResponse createLoan(CreateLoanRequest request) {

        if (request.userType() != 1 && request.userType() != 2 && request.userType() != 3) {
            throw new BusinessRuleException("Tipo de usuario no permitido en la biblioteca");
        }

        Optional<User> userByIdentification = userRepository.findByIdentification(request.userIdentification());
        if (userByIdentification.isEmpty()) {
            throw new ResourceNotFoundException("El usuario con identificación: "+ request.userIdentification() +" no existe. ");
        }
        User userExists = userByIdentification.get();

        if (!userExists.getUserType().equals(request.userType())) {
            throw new BusinessRuleException("El tipo de usuario enviado no coincide con el registrado en el sistema.");
        }

        Optional<Book> bookByIsbn = bookRepository.findByIsbn(request.isbn());
        if (bookByIsbn.isEmpty()) {
            throw new ResourceNotFoundException("El libro con ISBN: "+ request.isbn() +" no existe. ");
        }
        Book bookExists = bookByIsbn.get();

        if (!bookExists.getAvailable()) {
            throw new BusinessRuleException("El libro no está disponible para préstamo.");
        }

        // Regla del Invitado (usuario tipo 3)
        if (request.userType() == 3) {
            boolean hasActiveLoan = loanRepository.existsByUserIdentificationAndUserTypeAndStatus(
                    request.userIdentification(), 3, "ACTIVO");

            if (hasActiveLoan) {
                throw new BusinessRuleException("El usuario con identificación " + request.userIdentification() +
                        " ya tiene un libro prestado por lo cual no se le puede realizar otro préstamo");
            }
        }

        // Calcular fechas
        LocalDate today = LocalDate.now();
        int daysToAdd = switch (request.userType()) {
            case 1 -> 10; // Afiliado
            case 2 -> 8;  // Empleado
            case 3 -> 7;  // Invitado
            default -> 0;
        };

        LocalDate maxReturnDate = calculateBusinessDays(today, daysToAdd);


        Loan loan = Loan.builder()
                .isbn(bookExists.getIsbn())
                .userIdentification(userExists.getIdentification())
                .userType(userExists.getUserType())
                .loanDate(today)
                .maxReturnDate(maxReturnDate)
                .status("ACTIVO")
                .build();

        Loan savedLoan = loanRepository.save(loan);

        bookExists.setAvailable(false);
        bookRepository.save(bookExists);


        return LoanDetailResponse.fromEntity(savedLoan);
    }

    private LocalDate calculateBusinessDays(LocalDate startDate, int businessDays) {
        LocalDate resultDate = startDate;
        int addedDays = 0;

        while (addedDays < businessDays) {
            resultDate = resultDate.plusDays(1);

            // Si el día es sábado o domingo no cuenta
            if (resultDate.getDayOfWeek() != DayOfWeek.SATURDAY &&
                    resultDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                addedDays++;
            }
        }
        return resultDate;
    }

    public LoanDetailResponse getLoanById(Long loanId, User authenticatedUser) {

        Optional<Loan> loanById = loanRepository.findById(loanId);
        if (loanById.isEmpty()) {
            throw new ResourceNotFoundException("El préstamo con id: "+ loanId +" no existe. ");
        }
        Loan loanExists = loanById.get();

        if (authenticatedUser.getUserType() != 2 && !loanExists.getUserIdentification().equals(authenticatedUser.getIdentification())) {
            throw new ForbiddenActionException("No tienes permisos para consultar el préstamo de otro usuario.");
        }

        return LoanDetailResponse.fromEntity(loanExists);
    }

    public List<LoanDetailResponse> getLoans(String identificacionUsuario) {
        List<Loan> loans;

        if (identificacionUsuario != null && !identificacionUsuario.isBlank()) {
            loans = loanRepository.findByStatusAndUserIdentification("ACTIVO", identificacionUsuario);
        } else {
            // Si no se envía es porque son admin, en el controller hay @PreAuthorize
            loans = loanRepository.findByStatus("ACTIVO");
        }

        return loans.stream()
                .map(LoanDetailResponse::fromEntity)
                .toList();
    }


}
