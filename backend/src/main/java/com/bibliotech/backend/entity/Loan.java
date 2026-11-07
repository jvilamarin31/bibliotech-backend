package com.bibliotech.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String isbn;

    @Column(name = "user_identification", nullable = false, length = 50)
    private String userIdentification;

    @Column(name = "user_type", nullable = false)
    private Integer userType;

    @Column(name = "loan_date", nullable = false)
    private LocalDate loanDate;

    @Column(name = "max_return_date", nullable = false)
    private LocalDate maxReturnDate;

    @Column(nullable = false, length = 20)
    private String status;

}
