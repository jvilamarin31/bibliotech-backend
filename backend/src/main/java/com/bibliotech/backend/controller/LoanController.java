package com.bibliotech.backend.controller;

import com.bibliotech.backend.dto.loan.request.CreateLoanRequest;
import com.bibliotech.backend.dto.loan.response.LoanDetailResponse;
import com.bibliotech.backend.entity.User;
import com.bibliotech.backend.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prestamos")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<LoanDetailResponse> createLoan(@Valid @RequestBody CreateLoanRequest request) {
        LoanDetailResponse response = loanService.createLoan(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDetailResponse> getLoanById(@PathVariable Long id, @AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(loanService.getLoanById(id, authenticatedUser));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #identificacionUsuario == authentication.principal.identification")
    public ResponseEntity<List<LoanDetailResponse>> getLoans(
            @RequestParam(required = false) String identificacionUsuario) {
        return ResponseEntity.ok(loanService.getLoans(identificacionUsuario));
    }


}
