
package com.Digital_Bank.savings.controller;

import com.Digital_Bank.savings.dto.request.CreateSavingsRequest;
import com.Digital_Bank.savings.dto.response.SavingsResponse;
import com.Digital_Bank.savings.service.SavingsService;
import com.Digital_Bank.shared.constants.ApiConstants;
import com.Digital_Bank.shared.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH+"/savings")
@RequiredArgsConstructor
public class SavingsController {

    private final SavingsService savingsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SavingsResponse> create(
            @Valid @RequestBody CreateSavingsRequest request
    ) {
        return ApiResponse.success(
                "Savings account created successfully",
                savingsService.create(request)
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<SavingsResponse> findById(
            @PathVariable UUID id
    ) {
        return ApiResponse.success(
                "Savings account found successfully",
                savingsService.findById(id)
        );
    }

    @GetMapping("/reference/{reference}")
    public ApiResponse<SavingsResponse> findByReference(
            @PathVariable String reference
    ) {
        return ApiResponse.success(
                "Savings account found successfully",
                savingsService.findByReference(reference)
        );
    }

    @GetMapping("/account/{accountNumber}")
    public ApiResponse<Page<SavingsResponse>> findByAccount(
            @PathVariable String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = createPageable(page, size);

        return ApiResponse.success(
                "Savings accounts retrieved successfully",
                savingsService.findByAccountNumber(
                        accountNumber,
                        pageable
                )
        );
    }

    @PostMapping("/{id}/deposit")
    public ApiResponse<SavingsResponse> deposit(
            @PathVariable UUID id,
            @RequestParam BigDecimal amount
    ) {
        return ApiResponse.success(
                "Savings deposit completed successfully",
                savingsService.deposit(id, amount)
        );
    }

    @PostMapping("/{id}/withdraw")
    public ApiResponse<SavingsResponse> withdraw(
            @PathVariable UUID id,
            @RequestParam BigDecimal amount
    ) {
        return ApiResponse.success(
                "Savings withdrawal completed successfully",
                savingsService.withdraw(id, amount)
        );
    }

    @PostMapping("/{id}/interest")
    public ApiResponse<SavingsResponse> calculateInterest(
            @PathVariable UUID id
    ) {
        return ApiResponse.success(
                "Savings interest calculated successfully",
                savingsService.calculateInterest(id)
        );
    }
    @PatchMapping("/{id}/close")
    public ApiResponse<SavingsResponse> close(
            @PathVariable UUID id
    ) {
        return ApiResponse.success(
                "Savings account closed successfully",
                savingsService.close(id)
        );
    }

    private Pageable createPageable(int page, int size) {

        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Invalid pagination parameters"
            );
        }

        return PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
    }
}
