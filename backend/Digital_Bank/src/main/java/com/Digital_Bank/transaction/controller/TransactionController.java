
package com.Digital_Bank.transaction.controller;

import com.Digital_Bank.shared.constants.ApiConstants;
import com.Digital_Bank.transaction.domain.emuns.TransactionStatus;
import com.Digital_Bank.transaction.domain.emuns.TransactionType;
import com.Digital_Bank.transaction.dto.request.CreateTransactionRequest;
import com.Digital_Bank.transaction.dto.response.TransactionResponse;
import com.Digital_Bank.transaction.service.TransactionService;
import com.Digital_Bank.shared.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(ApiConstants.API_BASE_PATH +"/transactions")
@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TransactionResponse> deposit(
            @Valid @RequestBody CreateTransactionRequest request
    ) {
        TransactionResponse response = transactionService.deposit(request);

        return ApiResponse.success(
                "Deposit completed successfully",
                response
        );
    }

    @PostMapping("/withdraw")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TransactionResponse> withdraw(
            @Valid @RequestBody CreateTransactionRequest request
    ) {
        TransactionResponse response = transactionService.withdraw(request);

        return ApiResponse.success(
                "Withdrawal completed successfully",
                response
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<TransactionResponse> findById(
            @PathVariable UUID id
    ) {
        TransactionResponse response = transactionService.findById(id);

        return ApiResponse.success(
                "Transaction retrieved successfully",
                response
        );
    }

    @GetMapping("/reference/{reference}")
    public ApiResponse<TransactionResponse> findByReference(
            @PathVariable String reference
    ) {
        TransactionResponse response =
                transactionService.findByReference(reference);

        return ApiResponse.success(
                "Transaction retrieved successfully",
                response
        );
    }

    @GetMapping("/account/{accountId}")
    public ApiResponse<Page<TransactionResponse>> findByAccount(
            @PathVariable UUID accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<TransactionResponse> response =
                transactionService.findByAccount(accountId, pageable);

        return ApiResponse.success(
                "Account transactions retrieved successfully",
                response
        );
    }

    @GetMapping("/account/{accountId}/type/{type}")
    public ApiResponse<Page<TransactionResponse>> findByAccountAndType(
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

        Page<TransactionResponse> response =
                transactionService.findByAccountAndType(
                        accountId, type, pageable
                );

        return ApiResponse.success(
                "Account transactions filtered by type successfully",
                response
        );
    }

    @GetMapping("/account/{accountId}/status/{status}")
    public ApiResponse<Page<TransactionResponse>> findByAccountAndStatus(
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

        Page<TransactionResponse> response =
                transactionService.findByAccountAndStatus(
                        accountId, status, pageable
                );

        return ApiResponse.success(
                "Account transactions filtered by status successfully",
                response
        );
    }

    @GetMapping("/type/{type}")
    public ApiResponse<Page<TransactionResponse>> findByType(
            @PathVariable TransactionType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<TransactionResponse> response =
                transactionService.findByType(type, pageable);

        return ApiResponse.success(
                "Transactions filtered by type successfully",
                response
        );
    }

    @GetMapping("/status/{status}")
    public ApiResponse<Page<TransactionResponse>> findByStatus(
            @PathVariable TransactionStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<TransactionResponse> response =
                transactionService.findByStatus(status, pageable);

        return ApiResponse.success(
                "Transactions filtered by status successfully",
                response
        );
    }
}
