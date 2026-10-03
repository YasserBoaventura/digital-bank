package com.Digital_Bank.transaction.repository;

import com.Digital_Bank.transaction.domain.emuns.TransactionStatus;
import com.Digital_Bank.transaction.domain.emuns.TransactionType;
import com.Digital_Bank.transaction.domain.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository  extends JpaRepository<Transaction, UUID> {

    Page<Transaction> findByAccountId(
            UUID accountId,
            Pageable pageable
    );

    Page<Transaction> findByAccountIdAndType(
            UUID accountId,
            TransactionType type,
            Pageable pageable
    );

    Page<Transaction> findByAccountIdAndStatus(
            UUID accountId,
            TransactionStatus status,
            Pageable pageable
    );

    Page<Transaction> findByType(
            TransactionType type,
            Pageable pageable
    );

    Page<Transaction> findByStatus(
            TransactionStatus status,
            Pageable pageable
    );

    Optional<Transaction> findByReference(
            String reference
    );

    boolean existsByReference(
            String reference
    );

}
