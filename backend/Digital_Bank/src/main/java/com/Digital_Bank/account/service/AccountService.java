package com.Digital_Bank.account.service;

import com.Digital_Bank.account.domain.entity.Account;
import com.Digital_Bank.account.domain.enums.AccountStatus;
import com.Digital_Bank.account.domain.enums.AccountType;
import com.Digital_Bank.account.dto.request.CreateAccountRequest;
import com.Digital_Bank.account.dto.response.AccountResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AccountService {

    AccountResponse create(CreateAccountRequest request);

    AccountResponse findById(UUID id);

    AccountResponse findByAccountNumber(String accountNumber);

    List<AccountResponse> findByCustomer(UUID customerId);

    Page<AccountResponse> findByCustomer(
            UUID customerId,
            Pageable pageable
    );

    Page<AccountResponse> findByStatus(
            AccountStatus status,
            Pageable pageable
    );

    Page<AccountResponse> findByType(
            AccountType type,
            Pageable pageable
    );

    AccountResponse activate(UUID id);

    AccountResponse deactivate(UUID id);

    AccountResponse block(UUID id);


    List<Account> findAll();
}
