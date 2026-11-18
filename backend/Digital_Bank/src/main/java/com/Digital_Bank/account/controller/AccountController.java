
package com.Digital_Bank.account.controller;

import com.Digital_Bank.account.domain.enums.AccountStatus;
import com.Digital_Bank.account.domain.enums.AccountType;
import com.Digital_Bank.account.dto.request.CreateAccountRequest;
import com.Digital_Bank.account.dto.response.AccountResponse;
import com.Digital_Bank.account.service.AccountService;
import com.Digital_Bank.shared.constants.ApiConstants;
import com.Digital_Bank.shared.response.ApiResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH +"/accounts")
@RequiredArgsConstructor
@Validated
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> create(
            @Valid @RequestBody CreateAccountRequest request
    ) {
        AccountResponse response = accountService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Account created successfully",
                        response
                ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountResponse>>> findAll() {

        List<AccountResponse> response = accountService.findAll();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Accounts retrieved successfully",
                        response
                )
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountResponse>> findById(
            @PathVariable UUID id
    ) {
        AccountResponse response = accountService.findById(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Account retrieved successfully",
                        response
                )
        );
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountResponse>> findByAccountNumber(
            @PathVariable String accountNumber
    ) {
        AccountResponse response =
                accountService.findByAccountNumber(accountNumber);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Account retrieved successfully",
                        response
                )
        );
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<Page<AccountResponse>>> findByCustomer(
            @PathVariable UUID customerId,
            @RequestParam(defaultValue = "0")
            @Min(0) int page,
            @RequestParam(defaultValue = "10")
            @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<AccountResponse> response =
                accountService.findByCustomer(customerId, pageable);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer accounts retrieved successfully",
                        response
                )
        );
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<Page<AccountResponse>>> findByStatus(
            @PathVariable AccountStatus status,
            @RequestParam(defaultValue = "0")
            @Min(0) int page,
            @RequestParam(defaultValue = "10")
            @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<AccountResponse> response =
                accountService.findByStatus(status, pageable);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Accounts retrieved successfully",
                        response
                )
        );
    }


    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<Page<AccountResponse>>> findByType(
            @PathVariable AccountType type,
            @RequestParam(defaultValue = "0")
            @Min(0) int page,
            @RequestParam(defaultValue = "10")
            @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<AccountResponse> response =
                accountService.findByType(type, pageable);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Accounts retrieved successfully",
                        response
                )
        );
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<AccountResponse>> activate(
            @PathVariable UUID id
    ) {
        AccountResponse response = accountService.activate(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Account activated successfully",
                        response
                )
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<AccountResponse>> deactivate(
            @PathVariable UUID id
    ) {
        AccountResponse response = accountService.deactivate(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Account deactivated successfully",
                        response
                )
        );
    }

@PatchMapping("/{id}/block")
public ResponseEntity<ApiResponse<AccountResponse>> block(
        @PathVariable UUID id
) { AccountResponse response = accountService.block(id);

    return ResponseEntity.ok(
            ApiResponse.success(
                    "Account blocked successfully",
                    response
            )
    );
}

    @PatchMapping("/{id}/unlock")
    public ResponseEntity<ApiResponse<AccountResponse>> unlock(
            @PathVariable UUID id
    ) {
        AccountResponse response = accountService.unlock(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Account unlock successfully",
                        response
                )
        );
    }

}