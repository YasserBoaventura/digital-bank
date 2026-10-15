package com.Digital_Bank.savings.repository;

import com.Digital_Bank.savings.domain.entity.Savings;
import com.Digital_Bank.savings.domain.enums.SavingsStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingsRepository          extends JpaRepository<Savings, UUID> {


        Optional<Savings> findByReference(String reference);

        Page<Savings> findByAccountAccountNumber(
                String accountNumber,
                Pageable pageable
        );

        Page<Savings> findByAccountAccountNumberAndStatus(
                String accountNumber,
                SavingsStatus status,
                Pageable pageable
        );

        boolean existsByReference(String reference);

        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("""
        SELECT s FROM Savings s
        WHERE s.id = :id
    """)
        Optional<Savings> findByIdForUpdate(
                @Param("id") UUID id
        );

}
