package com.bibliotech.backend.service;

import com.bibliotech.backend.common.exception.BusinessRuleException;
import com.bibliotech.backend.common.exception.ResourceNotFoundException;
import com.bibliotech.backend.dto.loan.request.CreateLoanRequest;
import com.bibliotech.backend.dto.loan.response.LoanDetailResponse;
import com.bibliotech.backend.entity.Book;
import com.bibliotech.backend.entity.Loan;
import com.bibliotech.backend.entity.User;
import com.bibliotech.backend.repository.BookRepository;
import com.bibliotech.backend.repository.LoanRepository;
import com.bibliotech.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    private static final String ISBN = "978-3-16-148410-0";
    private static final String IDENTIFICATION = "123456789";

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoanService loanService;

    @Test
    void createLoanThrowsResourceNotFoundWhenUserDoesNotExist() {
        CreateLoanRequest request = buildRequest(1);
        when(userRepository.findByIdentification(IDENTIFICATION)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loanService.createLoan(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(IDENTIFICATION);

        verify(bookRepository, never()).findByIsbn(anyString());
        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void createLoanThrowsBusinessRuleWhenBookIsNotAvailable() {
        CreateLoanRequest request = buildRequest(1);
        when(userRepository.findByIdentification(IDENTIFICATION)).thenReturn(Optional.of(buildUser(1)));
        when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.of(buildBook(false)));

        assertThatThrownBy(() -> loanService.createLoan(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("El libro no está disponible para préstamo.");

        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void createLoanThrowsBusinessRuleWhenGuestHasActiveLoan() {
        CreateLoanRequest request = buildRequest(3);
        when(userRepository.findByIdentification(IDENTIFICATION)).thenReturn(Optional.of(buildUser(3)));
        when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.of(buildBook(true)));
        when(loanRepository.existsByUserIdentificationAndUserTypeAndStatus(
                IDENTIFICATION, 3, "ACTIVO")).thenReturn(true);

        assertThatThrownBy(() -> loanService.createLoan(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("El usuario con identificación " + IDENTIFICATION
                        + " ya tiene un libro prestado por lo cual no se le puede realizar otro préstamo");

        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void createLoanThrowsBusinessRuleWhenRequestedUserTypeDoesNotMatchStoredUser() {
        CreateLoanRequest request = buildRequest(2);
        when(userRepository.findByIdentification(IDENTIFICATION)).thenReturn(Optional.of(buildUser(1)));

        assertThatThrownBy(() -> loanService.createLoan(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("El tipo de usuario enviado no coincide con el registrado en el sistema.");

        verify(bookRepository, never()).findByIsbn(anyString());
        verify(loanRepository, never()).save(any(Loan.class));
    }

    @ParameterizedTest
    @CsvSource({
            "1,10",
            "2,8",
            "3,7"
    })
    void createLoanCalculatesMaxReturnDateSkippingWeekends(int userType, int expectedBusinessDays) {
        CreateLoanRequest request = buildRequest(userType);
        when(userRepository.findByIdentification(IDENTIFICATION)).thenReturn(Optional.of(buildUser(userType)));
        when(bookRepository.findByIsbn(ISBN)).thenReturn(Optional.of(buildBook(true)));
        if (userType == 3) {
            when(loanRepository.existsByUserIdentificationAndUserTypeAndStatus(
                    IDENTIFICATION, 3, "ACTIVO")).thenReturn(false);
        }
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> {
            Loan loan = invocation.getArgument(0);
            loan.setId(1L);
            return loan;
        });
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LoanDetailResponse response = loanService.createLoan(request);

        assertThat(response.maxReturnDate().getDayOfWeek())
                .isNotIn(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
        assertThat(countBusinessDays(response.loanDate(), response.maxReturnDate()))
                .isEqualTo(expectedBusinessDays);

        ArgumentCaptor<Loan> loanCaptor = ArgumentCaptor.forClass(Loan.class);
        verify(loanRepository).save(loanCaptor.capture());
        assertThat(loanCaptor.getValue().getStatus()).isEqualTo("ACTIVO");
        assertThat(loanCaptor.getValue().getMaxReturnDate()).isEqualTo(response.maxReturnDate());

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());
        assertThat(bookCaptor.getValue().getAvailable()).isFalse();
    }

    private CreateLoanRequest buildRequest(int userType) {
        return new CreateLoanRequest(ISBN, IDENTIFICATION, userType);
    }

    private User buildUser(int userType) {
        return User.builder()
                .id(1L)
                .name("Usuario de prueba")
                .identification(IDENTIFICATION)
                .userType(userType)
                .username("usuario")
                .password("password")
                .build();
    }

    private Book buildBook(boolean available) {
        return Book.builder()
                .id(1L)
                .isbn(ISBN)
                .title("Libro de prueba")
                .author("Autor de prueba")
                .available(available)
                .build();
    }

    private long countBusinessDays(LocalDate startDate, LocalDate endDate) {
        long businessDays = 0;
        for (LocalDate date = startDate.plusDays(1); !date.isAfter(endDate); date = date.plusDays(1)) {
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                businessDays++;
            }
        }
        return businessDays;
    }
}
