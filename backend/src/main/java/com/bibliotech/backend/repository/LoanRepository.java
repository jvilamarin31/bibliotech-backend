package com.bibliotech.backend.repository;

import com.bibliotech.backend.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByStatus(String status);
    List<Loan> findByStatusAndUserIdentification(String status, String userIdentification);

    boolean existsByUserIdentificationAndUserTypeAndStatus(String userIdentification, Integer userType, String status);
}
