package com.Digital_Bank.loan.controller;


import com.Digital_Bank.loan.domain.enums.LoanStatus;
import com.Digital_Bank.loan.domain.enums.LoanType;
import com.Digital_Bank.loan.dto.request.CreateLoanRequest;
import com.Digital_Bank.loan.dto.request.ReviewLoanRequest;
import com.Digital_Bank.loan.dto.response.LoanResponse;
import com.Digital_Bank.loan.service.LoanService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@Validated
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanResponse> create(
            @Valid @RequestBody CreateLoanRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(loanService.create(request));
    }
    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> findById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(loanService.findById(id));
    }

    @GetMapping("/reference/{reference}")
    public ResponseEntity<LoanResponse> findByReference(
            @PathVariable String reference
    ) {
        return ResponseEntity.ok(
                loanService.findByReference(reference)
        );
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<Page<LoanResponse>> findByAccount(
            @PathVariable String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                loanService.findByAccountNumber(
                        accountNumber, createPageable(page, size)
                )
        );
    }

    @GetMapping("/account/{accountNumber}/status/{status}")
    public ResponseEntity<Page<LoanResponse>> findByAccountAndStatus(
            @PathVariable String accountNumber,
            @PathVariable LoanStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                loanService.findByAccountNumberAndStatus(
                        accountNumber, status, createPageable(page, size)
                )
        );
    }

    @GetMapping("/account/{accountNumber}/type/{type}")
    public ResponseEntity<Page<LoanResponse>> findByAccountAndType(
            @PathVariable String accountNumber,
            @PathVariable LoanType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                loanService.findByAccountNumberAndType(
                        accountNumber, type, createPageable(page, size)
                )
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<LoanResponse>> findByStatus(
            @PathVariable LoanStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                loanService.findByStatus(
                        status, createPageable(page, size)
                )
        );
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<LoanResponse>> findByType(
            @PathVariable LoanType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                loanService.findByType(type, createPageable(page, size))
        );
    }
    @PatchMapping("/{id}/review")
    public ResponseEntity<LoanResponse> review(
            @PathVariable UUID id,
            @Valid @RequestBody ReviewLoanRequest request
    ) {
        return ResponseEntity.ok(loanService.review(id, request));
    }

    @PostMapping("/{id}/disburse")
    public ResponseEntity<LoanResponse> disburse(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(loanService.disburse(id));
    }

    @PostMapping("/{id}/repay")
    public ResponseEntity<LoanResponse> repay(
            @PathVariable UUID id,
            @RequestParam
            @DecimalMin(value = "0.01", message = "Amount must be positive")
            BigDecimal amount
    ) {
        return ResponseEntity.ok(loanService.repay(id, amount));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<LoanResponse> cancel(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(loanService.cancel(id));
    }

    private Pageable createPageable(int page, int size) {
        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page must be >= 0 and size must be between 1 and 100"
            );
        }

        return PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

    }
}
