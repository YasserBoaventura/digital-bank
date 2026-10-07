package com.Digital_Bank.transaction.service.impl;

import com.Digital_Bank.account.domain.entity.Account;
import com.Digital_Bank.account.repository.AccountRepository;
import com.Digital_Bank.transaction.domain.emuns.TransactionStatus;
import com.Digital_Bank.transaction.domain.emuns.TransactionType;
import com.Digital_Bank.transaction.domain.entity.Transaction;
import com.Digital_Bank.transaction.dto.request.CreateTransactionRequest;
import com.Digital_Bank.transaction.dto.response.TransactionResponse;
import com.Digital_Bank.transaction.repository.TransactionRepository;
import com.Digital_Bank.transaction.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl  implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public TransactionResponse deposit(
            CreateTransactionRequest request
    ) {

        Account account = findByAccountNumberForUpdate(
                request.accountNumber()
        );

        validateAmount(request.amount());

        BigDecimal balanceBefore =
                getBalance(account);

        BigDecimal balanceAfter =
                balanceBefore.add(request.amount());

        account.setBalance(balanceAfter);

        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .account(account)
                .type(TransactionType.DEPOSIT)
                .status(TransactionStatus.COMPLETED)
                .amount(request.amount())
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .description(request.description())
                .build();

        Transaction saved =
                transactionRepository.save(transaction);

        return toResponse(saved);
    }

    @Transactional
    public TransactionResponse withdraw(
            CreateTransactionRequest request
    ) {
        Account account = findByAccountNumberForUpdate(
                request.accountNumber()
        );

        validateAmount(request.amount());

        BigDecimal balanceBefore =
                getBalance(account);

        if (balanceBefore.compareTo(request.amount()) < 0) {

            throw new IllegalStateException(
                    "Insufficient account balance"
            );
        }

        BigDecimal balanceAfter =
                balanceBefore.subtract(
                        request.amount()
                );

        account.setBalance(balanceAfter);

        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .account(account)
                .type(TransactionType.WITHDRAWAL)
                .status(TransactionStatus.COMPLETED)
                .amount(request.amount())
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .description(request.description())
                .build();

        Transaction saved =
                transactionRepository.save(transaction);

        return toResponse(saved);
    }

        @Transactional(readOnly = true)
        public TransactionResponse findById(
                UUID id
        ) {

    Transaction transaction =
            transactionRepository.findById(id)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Transaction not found: " + id
                            )
                    );

    return toResponse(transaction);
    }

        @Transactional(readOnly = true)
        public TransactionResponse findByReference(
                String reference
        ) {

    Transaction transaction =
            transactionRepository
                    .findByReference(reference)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Transaction not found with reference: "
                                            + reference
                            )
                    );

    return toResponse(transaction);
    }

        @Transactional(readOnly = true)
        public Page<TransactionResponse> findByAccount(
                UUID accountId,
                Pageable pageable
        ) {

            return transactionRepository
                    .findByAccountId(
                            accountId,
                            pageable
                    )
                    .map(this::toResponse);
        }

        @Transactional(readOnly = true)
        public Page<TransactionResponse> findByAccountAndType(
                UUID accountId,
                TransactionType type,
                Pageable pageable
        ) {

            return transactionRepository
                    .findByAccountIdAndType(
                            accountId,
                            type,
                            pageable
                    )
                    .map(this::toResponse);
        }

        @Transactional(readOnly = true)
        public Page<TransactionResponse> findByAccountAndStatus(
                UUID accountId,
                TransactionStatus status,
                Pageable pageable
        ) {

            return transactionRepository
                    .findByAccountIdAndStatus(
                            accountId,
                            status,
                            pageable
                    )
                    .map(this::toResponse);
        }

        @Transactional(readOnly = true)
        public Page<TransactionResponse> findByType(
                TransactionType type,
                Pageable pageable
        ) {

            return transactionRepository
                    .findByType(type, pageable)
                    .map(this::toResponse);
        }

        @Transactional(readOnly = true)
        public Page<TransactionResponse> findByStatus(
                TransactionStatus status,
                Pageable pageable
        ) {

            return transactionRepository
                    .findByStatus(status, pageable)
                    .map(this::toResponse);
        }

        private Account findByAccountNumberForUpdate(
                String accoutNumber
        ) {

            return accountRepository
                    .findByAccountNumber(accoutNumber)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Account not found: " + accoutNumber
                            )
                    );
        }

        private void validateAmount(
                BigDecimal amount
        ) {
            if (amount == null ||
                    amount.compareTo(BigDecimal.ZERO) <= 0) {

                throw new IllegalArgumentException(
                        "Transaction amount must be greater than zero"
                );
            }
        }

        private BigDecimal getBalance(
                Account account
        ) {

            if (account.getBalance() == null) {
                return BigDecimal.ZERO;
            }

            return account.getBalance();
        }

        private TransactionResponse toResponse(
                Transaction transaction
        ) {

            return new TransactionResponse(
                    transaction.getId(),
                    transaction.getAccount().getId(),
                    transaction.getType(),
                    transaction.getStatus(),
                    transaction.getAmount(),
                    transaction.getBalanceBefore(),
                    transaction.getBalanceAfter(),
                    transaction.getDescription(),
                    transaction.getReference(),
                    transaction.getCreatedAt(),
                    transaction.getUpdatedAt()
            );
        }

    }
