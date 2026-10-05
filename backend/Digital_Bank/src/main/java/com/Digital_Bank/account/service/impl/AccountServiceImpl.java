package com.Digital_Bank.account.service.impl;
import com.Digital_Bank.account.domain.entity.Account;
import com.Digital_Bank.account.domain.enums.AccountStatus;
import com.Digital_Bank.account.domain.enums.AccountType;
import com.Digital_Bank.account.dto.request.CreateAccountRequest;
import com.Digital_Bank.account.dto.response.AccountResponse;
import com.Digital_Bank.account.repository.AccountRepository;
import com.Digital_Bank.account.service.AccountService;
import com.Digital_Bank.account.shared.exception.BussinessException;
import com.Digital_Bank.costumer.domain.entity.Customer;
import com.Digital_Bank.costumer.domain.enums.CustomerStatus;
import com.Digital_Bank.costumer.repository.CustomerRepository;

import com.Digital_Bank.costumer.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Override
    public AccountResponse create(CreateAccountRequest request) {

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cliente não encontrado"
                        )
                );
        if (customer.getStatus() == CustomerStatus.BLOCKED) {
            throw new BussinessException(
                    "Cliente bloqueado não pode abrir uma conta."
            );

        }

        String accountNumber = generateAccountNumber();

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .type(request.type())
                .status(AccountStatus.ACTIVE)
                .balance(java.math.BigDecimal.ZERO)
                .currency("MZN")
                .customer(customer)
                .build();

        Account savedAccount = accountRepository.save(account);

        return toResponse(savedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse findById(UUID id) {
        Account account = findAccount(id);

        return toResponse(account);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse findByAccountNumber(
            String accountNumber) {
    Account account = accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Conta não encontrada"
                    )
            );

        return toResponse(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> findByCustomer(
            UUID customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException(
                    "Cliente não encontrado"
            );
        }

    return accountRepository
            .findByCustomerId(customerId)
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountResponse> findByCustomer(
            UUID customerId,
            Pageable pageable) {

        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException(
                    "Cliente não encontrado"
            );
        }
        return accountRepository
                .findByCustomerId(customerId, pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountResponse> findByStatus(
            AccountStatus status,
            Pageable pageable) {
        return accountRepository
                .findByStatus(status, pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountResponse> findByType(
            AccountType type,
            Pageable pageable) {
        return accountRepository
                .findByType(type, pageable)
                .map(this::toResponse);
    }

    @Override
    public AccountResponse activate(UUID id) {
        Account account = findAccount(id);

        if (account.getStatus() == AccountStatus.BLOCKED) {
            throw new BussinessException(
                    "Conta bloqueada não pode ser ativada."
            );
        }
        account.setStatus(AccountStatus.ACTIVE);

        return toResponse(accountRepository.save(account));
    }

    @Override
    public AccountResponse deactivate(UUID id) {

        Account account = findAccount(id);

        if (account.getStatus() == AccountStatus.BLOCKED) {
            throw new BussinessException(
                    "Conta bloqueada não pode ser desativada."
            );
        }

        account.setStatus(AccountStatus.INACTIVE);

        return toResponse(accountRepository.save(account));
    }

    @Override
    public AccountResponse block(UUID id) {

        Account account = findAccount(id);

        if (account.getStatus() == AccountStatus.BLOCKED) {
            throw new BussinessException(
                    "Conta já está bloqueada."
            );
        }

        account.setStatus(AccountStatus.BLOCKED);

        return toResponse(accountRepository.save(account));
    }

    private Account findAccount(UUID id) {

        return accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Conta não encontrada"
                        )
                );
    }

    private String generateAccountNumber() {
        String accountNumber;

        do {
            accountNumber = String.valueOf(
                    ThreadLocalRandom.current()
                            .nextLong(1000000000L, 9999999999L)
            );

        } while (
                accountRepository.existsByAccountNumber(accountNumber)
        );

        return accountNumber;
    }

    private AccountResponse toResponse(Account account) {

        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getType(),
                account.getStatus(),
                account.getBalance(),
                account.getCurrency(),
                account.getCustomer().getId(),
                account.getCreatedAt(),
                account.getUpdatedAt()
        );
    }

    public List<Account> findAll(){
        return accountRepository.findAll();
    }
}
