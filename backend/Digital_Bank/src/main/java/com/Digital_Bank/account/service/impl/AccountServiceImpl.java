package com.Digital_Bank.account.service.impl;
import com.Digital_Bank.account.domain.entity.Account;
import com.Digital_Bank.account.domain.enums.AccountStatus;
import com.Digital_Bank.account.domain.enums.AccountType;
import com.Digital_Bank.account.dto.request.CreateAccountRequest;
import com.Digital_Bank.account.dto.response.AccountResponse;
import com.Digital_Bank.account.repository.AccountRepository;
import com.Digital_Bank.account.service.AccountService;
import com.Digital_Bank.account.shared.exception.BussinessException;
import com.Digital_Bank.audit.domain.enums.AuditAction;
import com.Digital_Bank.audit.service.AuditService;
import com.Digital_Bank.costumer.domain.entity.Customer;
import com.Digital_Bank.costumer.domain.enums.CustomerStatus;
import com.Digital_Bank.costumer.repository.CustomerRepository;
import java.math.*;
import com.Digital_Bank.costumer.shared.exception.ResourceNotFoundException;
import com.Digital_Bank.notification.domain.enums.NotificationType;
import com.Digital_Bank.notification.service.NotificationService;
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
    private final NotificationService notificationService;
    private final AuditService auditService;



    @Override
    @Transactional
    public AccountResponse create(CreateAccountRequest request) {

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found")
                );

        if (customer.getStatus() == CustomerStatus.BLOCKED) {
            throw new BussinessException(
                    "A blocked customer cannot open an account."
            );
        }

        String accountNumber = generateAccountNumber();

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .type(request.type())
                .status(AccountStatus.ACTIVE)
                .balance(BigDecimal.ZERO)
                .currency("MZN")
                .customer(customer)
                .build();

        Account savedAccount = accountRepository.save(account);
//notify
        notificationService.createForCustomer(
                customer.getId(),
                NotificationType.ACCOUNT,
                "Account Created Successfully",
                "Your account " + savedAccount.getAccountNumber()
                        + " has been created successfully."
        );
//audit
        auditService.record(
                null,
                AuditAction.CREATE,
                "ACCOUNT",
                savedAccount.getId().toString(),
                "Bank account created successfully"
        );

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
                            "account not found"
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
                    "customer not found"
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
                    "customer not found"
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
                    "Blocked account cannot be activated"
            );
        }
        account.setStatus(AccountStatus.ACTIVE);
        //save the notify
        notificationService.createForCustomer(
                account.getCustomer().getId(),
                NotificationType.ACCOUNT,
                "Account Activated",
                "Your account " + account.getAccountNumber()
                        + " has been activated successfully."
        );

        auditService.record(
                null,
                AuditAction.ACTIVATE,
                "Account",
                account.getId().toString(),
                "Card created successfully"
        );
        return toResponse(accountRepository.save(account));
    }

    @Override
    public AccountResponse deactivate(UUID id) {

        Account account = findAccount(id);

        if (account.getStatus() == AccountStatus.BLOCKED) {
            throw new BussinessException(
                    "Blocked account cannot be deactivated."
            );
        }

        account.setStatus(AccountStatus.INACTIVE);
        //save the notify
        notificationService.createForCustomer(
                account.getCustomer().getId(),
                NotificationType.ACCOUNT,
                "Account Deactivated",
                "Your account " + account.getAccountNumber()
                        + " has been deactivated."
        );
        //save de adit use
        auditService.record(
                null,
                AuditAction.CREATE,
                "Account",
                account.getId().toString(),
                "Account deactivated successfully"
        );
        return toResponse(accountRepository.save(account));
    }

    @Override
    public AccountResponse block(UUID id) {

        Account account = findAccount(id);

        if (account.getStatus() == AccountStatus.BLOCKED) {
            throw new BussinessException(
                    "The account is already blocked."
            );
        }
        //save the notify
        notificationService.createForCustomer(
                account.getCustomer().getId(),
                NotificationType.ACCOUNT,
                "Account Blocked",
                "Your account " + account.getAccountNumber()
                        + " has been blocked. Please contact the bank for assistance."
        );
        account.setStatus(AccountStatus.BLOCKED);

        auditService.record(
                null,
                AuditAction.CREATE,
                "Account",
                account.getId().toString(),
                "Card blocked successfully"
        );

        return toResponse(accountRepository.save(account));
    }

    private Account findAccount(UUID id) {

    return accountRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "account not found"
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
