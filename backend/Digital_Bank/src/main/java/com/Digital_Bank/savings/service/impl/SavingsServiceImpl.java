package com.Digital_Bank.savings.service.impl;



import com.Digital_Bank.account.domain.entity.Account;
import com.Digital_Bank.account.domain.enums.AccountStatus;
import com.Digital_Bank.account.repository.AccountRepository;

import com.Digital_Bank.savings.domain.entity.Savings;
import com.Digital_Bank.savings.domain.enums.SavingsStatus;
import com.Digital_Bank.savings.dto.request.CreateSavingsRequest;
import com.Digital_Bank.savings.dto.response.SavingsResponse;
import com.Digital_Bank.savings.repository.SavingsRepository;
import com.Digital_Bank.savings.service.SavingsService;
import com.Digital_Bank.savings.shared.exception.SavingsNotFoundException;
import com.Digital_Bank.savings.shared.exception.SavingsProcessingException;

import com.Digital_Bank.transaction.domain.emuns.TransactionStatus;
import com.Digital_Bank.transaction.domain.emuns.TransactionType;
import com.Digital_Bank.transaction.domain.entity.Transaction;

import com.Digital_Bank.transaction.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SavingsServiceImpl implements SavingsService {



        private static final BigDecimal ZERO =
                new BigDecimal("0.00");

        private static final BigDecimal DAYS_IN_YEAR =
                new BigDecimal("365");

        private final SavingsRepository savingsRepository;
        private final AccountRepository accountRepository;
        private final TransactionRepository transactionRepository;

@Override
@Transactional
public SavingsResponse create(CreateSavingsRequest request) {

    Account account = accountRepository
            .findByAccountNumberForUpdate(request.accountNumber())
            .orElseThrow(() ->
                    new SavingsProcessingException(
                            "Bank account not found"
                    )
            );

    if (account.getStatus() != AccountStatus.ACTIVE) {
        throw new SavingsProcessingException(
                "Only active accounts can open savings"
        );
    }

    Savings savings = Savings.builder()
            .account(account)
            .status(SavingsStatus.ACTIVE)
            .balance(ZERO)
            .annualInterestRate(
                    request.annualInterestRate()
                            .setScale(2, RoundingMode.HALF_UP)
            )

            .openedAt(LocalDateTime.now())
            .lastInterestCalculationAt(LocalDateTime.now())
            .build();

    return toResponse(savingsRepository.save(savings));
}

@Override
public SavingsResponse findById(UUID id) {
    return toResponse(findSavings(id));
}

@Override
public SavingsResponse findByReference(String reference) {

    Savings savings = savingsRepository
            .findByReference(reference)
            .orElseThrow(() ->
                    new SavingsNotFoundException(
                            "Savings account not found: " + reference
                    )
            );

    return toResponse(savings);
}

@Override
public Page<SavingsResponse> findByAccountNumber(
        String accountNumber,
        Pageable pageable) {

    return savingsRepository
            .findByAccountAccountNumber(accountNumber, pageable)
            .map(this::toResponse);
}

@Override
@Transactional
public SavingsResponse deposit(UUID id, BigDecimal amount) {

    validateAmount(amount);

    // Lock order: main account, then savings.
    Savings current = findSavings(id);
    String accountNumber =
            current.getAccount().getAccountNumber();

    Account account = accountRepository
            .findByAccountNumberForUpdate(accountNumber)
            .orElseThrow(() ->
                    new SavingsProcessingException(
                            "Bank account not found"
                    )
            );

    Savings savings = savingsRepository
            .findByIdForUpdate(id)
            .orElseThrow(() ->
                    new SavingsNotFoundException(
                            "Savings account not found: " + id
                    )
            );

    ensureSavingsActive(savings);
    ensureAccountActive(account);

    if (account.getBalance().compareTo(amount) < 0) {
        throw new SavingsProcessingException(
                "Insufficient balance in the main account"
        );
    }

    BigDecimal balanceBefore = account.getBalance();
    BigDecimal balanceAfter =
            balanceBefore.subtract(amount);

    account.setBalance(balanceAfter);
    savings.setBalance(savings.getBalance().add(amount));

transactionRepository.save(
        buildTransaction(
                account,
                amount,
                balanceBefore,
                balanceAfter,
                "Deposit into savings " + savings.getReference(),
                TransactionType.WITHDRAWAL
        )
);

    accountRepository.save(account);
    Savings saved = savingsRepository.save(savings);

    return toResponse(saved);
}

@Override
@Transactional
public SavingsResponse withdraw(UUID id, BigDecimal amount) {

    validateAmount(amount);

    Savings current = findSavings(id);
    String accountNumber =
            current.getAccount().getAccountNumber();

    Account account = accountRepository
            .findByAccountNumberForUpdate(accountNumber)
            .orElseThrow(() ->
                    new SavingsProcessingException(
                            "Bank account not found"
                    )
            );

    Savings savings = savingsRepository
            .findByIdForUpdate(id)
            .orElseThrow(() ->
                    new SavingsNotFoundException(
                            "Savings account not found: " + id
                    )
            );

    ensureSavingsActive(savings);
    ensureAccountActive(account);

    if (savings.getBalance().compareTo(amount) < 0) {
        throw new SavingsProcessingException(
                "Insufficient savings balance"
        );
    }

    BigDecimal balanceBefore = account.getBalance();
    BigDecimal balanceAfter =
            balanceBefore.add(amount);

    savings.setBalance(savings.getBalance().subtract(amount));
    account.setBalance(balanceAfter);

    transactionRepository.save(
    buildTransaction(
                account,
                amount,
                balanceBefore,
                balanceAfter,
                "Withdrawal from savings " + savings.getReference(),
                TransactionType.DEPOSIT
        )
    );

            accountRepository.save(account);
            Savings saved = savingsRepository.save(savings);

            return toResponse(saved);
        }

    @Override
    @Transactional
public SavingsResponse calculateInterest(UUID id) {

    Savings savings = savingsRepository
            .findByIdForUpdate(id)
            .orElseThrow(() ->
                    new SavingsNotFoundException(
                            "Savings account not found: " + id
                    )
            );

    ensureSavingsActive(savings);

    LocalDateTime now = LocalDateTime.now();

    long days = ChronoUnit.DAYS.between(
            savings.getLastInterestCalculationAt(),
            now
    );

    if (days <= 0) {
        throw new SavingsProcessingException(
                "No interest period has elapsed"
        );
    }

    BigDecimal interest = savings.getBalance()
            .multiply(savings.getAnnualInterestRate())
            .multiply(BigDecimal.valueOf(days))
            .divide(
                    new BigDecimal("100").multiply(DAYS_IN_YEAR),
                    2,
                    RoundingMode.HALF_UP
            );

    savings.setBalance(
            savings.getBalance().add(interest)
    );

    savings.setLastInterestCalculationAt(now);

    return toResponse(savingsRepository.save(savings));
}

    @Override
    @Transactional
    public SavingsResponse close(UUID id) {

        Savings savings = savingsRepository
                .findByIdForUpdate(id)
                .orElseThrow(() ->
                        new SavingsNotFoundException(
                                "Savings account not found: " + id
                        )
                );

        ensureSavingsActive(savings);

        if (savings.getBalance().compareTo(ZERO) != 0) {
            throw new SavingsProcessingException(
                    "Savings balance must be zero before closing"
            );
        }

        savings.setStatus(SavingsStatus.CLOSED);
        savings.setClosedAt(LocalDateTime.now());

        return toResponse(savingsRepository.save(savings));
    }

private Savings findSavings(UUID id) {
    return savingsRepository.findById(id)
            .orElseThrow(() ->
                    new SavingsNotFoundException(
                            "Savings account not found: " + id
                    )
            );
}

    private void validateAmount(BigDecimal amount) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SavingsProcessingException(
                    "Amount must be greater than zero"
            );
        }

        if (amount.scale() > 2) {
            throw new SavingsProcessingException(
                    "Amount cannot have more than two decimal places"
            );
        }
    }

    private void ensureSavingsActive(Savings savings) {
        if (savings.getStatus() != SavingsStatus.ACTIVE) {
            throw new SavingsProcessingException(
                    "Savings account is not active"
            );
        }
    }

    private void ensureAccountActive(Account account) {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new SavingsProcessingException(
                    "Main account is not active"
            );
        }
    }

    private Transaction buildTransaction(
            Account account,
            BigDecimal amount,
            BigDecimal before,
            BigDecimal after,
            String description,
            TransactionType type) {

        return Transaction.builder()
                .account(account)
                .type(type)
                .status(TransactionStatus.COMPLETED)
                .amount(amount)
                .balanceBefore(before)
                .balanceAfter(after)
                .description(description)
                .build();
}

private SavingsResponse toResponse(Savings savings) {
    return new SavingsResponse(
            savings.getId(),
            savings.getReference(),
            savings.getAccount().getAccountNumber(),
            savings.getStatus(),
            savings.getBalance(),
            savings.getAnnualInterestRate(),
            savings.getOpenedAt(),
            savings.getClosedAt(),
            savings.getLastInterestCalculationAt(),
            savings.getCreatedAt(),
            savings.getUpdatedAt()
    );
}

}
