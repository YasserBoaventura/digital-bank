package com.Digital_Bank.account.repository;

import com.Digital_Bank.account.domain.entity.Account;
import com.Digital_Bank.account.domain.enums.AccountStatus;
import com.Digital_Bank.account.domain.enums.AccountType;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

        Optional<Account> findByAccountNumber(String accountNumber);

        boolean existsByAccountNumber(String accountNumber);

        List<Account> findByCustomerId(UUID customerId);

        Page<Account> findByCustomerId(
                UUID customerId,
                Pageable pageable
        );

        Page<Account> findByStatus(
                AccountStatus status,
                Pageable pageable
        );

        Page<Account> findByType(
                AccountType type,
                Pageable pageable
        );

        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("""
        SELECT a 
        FROM Account a
        WHERE a.id = :id
        """)
        Optional<Account> findByIdForUpdate(
                @Param("id") UUID id
        );

}

