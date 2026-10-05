package com.Digital_Bank.account.controller;


import com.Digital_Bank.account.domain.entity.Account;
import com.Digital_Bank.account.domain.enums.AccountStatus;
import com.Digital_Bank.account.domain.enums.AccountType;
import com.Digital_Bank.account.dto.request.CreateAccountRequest;
import com.Digital_Bank.account.dto.response.AccountResponse;
import com.Digital_Bank.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

        private final AccountService accountService;

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public AccountResponse create(
                @Valid @RequestBody CreateAccountRequest request) {
            return accountService.create(request);
        }
    @GetMapping
    public List<Account> findAll() {
       return   accountService.findAll();
    }

    @GetMapping("/{id}")
        public AccountResponse findById(
                @PathVariable UUID id) {
            return accountService.findById(id);
        }

        @GetMapping("/number/{accountNumber}")
        public AccountResponse findByAccountNumber(
                @PathVariable String accountNumber) {
            return accountService.findByAccountNumber(accountNumber);
        }

        @GetMapping("/customer/{customerId}")
        public Page<AccountResponse> findByCustomer(
                @PathVariable UUID customerId,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size) {

            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by("createdAt").descending()
            );
            return accountService.findByCustomer(
                    customerId,
                    pageable
            );
        }
        @GetMapping("/status/{status}")
        public Page<AccountResponse> findByStatus(
                @PathVariable AccountStatus status,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size) {
            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by("createdAt").descending()
            );

            return accountService.findByStatus(
                    status,
                    pageable
            );
        }

        @GetMapping("/type/{type}")
        public Page<AccountResponse> findByType(
                @PathVariable AccountType type,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size) {

            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by("createdAt").descending()
            );
            return accountService.findByType(
                    type,
                    pageable
            );
        }
        @PatchMapping("/{id}/activate")
        public AccountResponse activate(
                @PathVariable UUID id) {
            return accountService.activate(id);
        }
        @PatchMapping("/{id}/deactivate")
        public AccountResponse deactivate(
                @PathVariable UUID id) {
            return accountService.deactivate(id);
        }
        @PatchMapping("/{id}/block")
        public AccountResponse block(
                @PathVariable UUID id) {
            return accountService.block(id);
        }





}
