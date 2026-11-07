package com.bibliotech.backend.dto.loan.response;

import com.bibliotech.backend.entity.Loan;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record LoanDetailResponse(
        Long id,

        @JsonProperty("isbn")
        String isbn,

        @JsonProperty("identificacionUsuario")
        String userIdentification,

        @JsonProperty("tipoUsuario")
        Integer userType,

        @JsonProperty("fechaPrestamo")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate loanDate,

        @JsonProperty("fechaMaximaDevolucion")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate maxReturnDate,

        @JsonProperty("estado")
        String status
) {
    public static LoanDetailResponse fromEntity(Loan loan) {
        return new LoanDetailResponse(
                loan.getId(),
                loan.getIsbn(),
                loan.getUserIdentification(),
                loan.getUserType(),
                loan.getLoanDate(),
                loan.getMaxReturnDate(),
                loan.getStatus()
        );
    }
}
