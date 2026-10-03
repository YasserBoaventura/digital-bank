package com.Digital_Bank.transaction.controller;

import com.Digital_Bank.transaction.domain.emuns.TransactionStatus;
import com.Digital_Bank.transaction.domain.emuns.TransactionType;
import com.Digital_Bank.transaction.dto.request.CreateTransactionRequest;
import com.Digital_Bank.transaction.dto.response.TransactionResponse;
import com.Digital_Bank.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/transactions")
@RestController
@RequiredArgsConstructor
public class TransactionController {

        private final TransactionService transactionService;

        @PostMapping("/deposit")
        @ResponseStatus(HttpStatus.CREATED)
        public TransactionResponse deposit(
                @Valid @RequestBody CreateTransactionRequest request
        ) {
            return transactionService.deposit(request);
        }
        @PostMapping("/withdraw")
        @ResponseStatus(HttpStatus.CREATED)
        public TransactionResponse withdraw(
                @Valid @RequestBody CreateTransactionRequest request
        ) {

            return transactionService.withdraw(request);
        }

        @GetMapping("/{id}")
        public TransactionResponse findById(
                @PathVariable UUID id
        ) {
            return transactionService.findById(id);
        }

        @GetMapping("/reference/{reference}")
        public TransactionResponse findByReference(
                @PathVariable String reference
        ) {

            return transactionService.findByReference(reference);
        }

        @GetMapping("/account/{accountId}")
        public Page<TransactionResponse> findByAccount(
                @PathVariable UUID accountId,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size
        ) {

            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by("createdAt").descending()
            );

            return transactionService.findByAccount(
                    accountId,
                    pageable
            );
        }

        @GetMapping("/account/{accountId}/type/{type}")
        public Page<TransactionResponse> findByAccountAndType(
                @PathVariable UUID accountId,
                @PathVariable TransactionType type,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size
        ) {

            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by("createdAt").descending()
            );

            return transactionService.findByAccountAndType(
                    accountId,
                    type,
                    pageable
            );
        }

        @GetMapping("/account/{accountId}/status/{status}")
        public Page<TransactionResponse> findByAccountAndStatus(
                @PathVariable UUID accountId,
                @PathVariable TransactionStatus status,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size
        ) {

            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by("createdAt").descending()
            );

            return transactionService.findByAccountAndStatus(
                    accountId,
                    status,
                    pageable
            );
        }

        @GetMapping("/type/{type}")
        public Page<TransactionResponse> findByType(
                @PathVariable TransactionType type,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size
        ) {

            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by("createdAt").descending()
            );

            return transactionService.findByType(
                    type,
                    pageable
            );
        }

        @GetMapping("/status/{status}")
        public Page<TransactionResponse> findByStatus(
                @PathVariable TransactionStatus status,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size
        ) {

            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by("createdAt").descending()
            );

            return transactionService.findByStatus(
                    status,
                    pageable
            );
        }

}
