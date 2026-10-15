package com.Digital_Bank.savings.controller;

import com.Digital_Bank.savings.dto.request.CreateSavingsRequest;
import com.Digital_Bank.savings.dto.response.SavingsResponse;
import com.Digital_Bank.savings.service.SavingsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/savings")
@RequiredArgsConstructor
public class SavingsController {

        private final SavingsService savingsService;

    @PostMapping
    public ResponseEntity<SavingsResponse> create(
            @Valid @RequestBody CreateSavingsRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savingsService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SavingsResponse> findById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                savingsService.findById(id)
        );
    }

    @GetMapping("/reference/{reference}")
    public ResponseEntity<SavingsResponse> findByReference(
            @PathVariable String reference) {

        return ResponseEntity.ok(
                savingsService.findByReference(reference)
        );
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<Page<SavingsResponse>> findByAccount(
            @PathVariable String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Invalid pagination parameters"
            );
        }
return ResponseEntity.ok(
        savingsService.findByAccountNumber(
                accountNumber,
                PageRequest.of(
                        page,
                        size,
                        Sort.by(Sort.Direction.DESC, "createdAt")
                )
        )
);
}
@PostMapping("/{id}/deposit")
    public ResponseEntity<SavingsResponse> deposit(
            @PathVariable UUID id,
            @RequestParam BigDecimal amount) {

        return ResponseEntity.ok(
                savingsService.deposit(id, amount)
        );
        }
    @PostMapping("/{id}/withdraw")
    public ResponseEntity<SavingsResponse> withdraw(
            @PathVariable UUID id,
            @RequestParam BigDecimal amount) {

        return ResponseEntity.ok(
                savingsService.withdraw(id, amount)
        );
    }

    @PostMapping("/{id}/interest")
    public ResponseEntity<SavingsResponse> calculateInterest(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                savingsService.calculateInterest(id)
        );
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<SavingsResponse> close(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                savingsService.close(id)
        );
    }

}
