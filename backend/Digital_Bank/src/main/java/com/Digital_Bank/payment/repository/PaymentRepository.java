package com.Digital_Bank.payment.repository;

import com.Digital_Bank.payment.domain.entity.Payment;
import com.Digital_Bank.payment.domain.enums.PaymentStatus;
import com.Digital_Bank.payment.domain.enums.PaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.io.*;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

        Optional<Payment> findByReference(String reference);

        boolean existsByReference(String reference);

        Page<Payment> findByAccountAccountNumber(
                String accountNumber,
                Pageable pageable
        );

        Page<Payment> findByAccountAccountNumberAndStatus(
                String accountNumber,
                PaymentStatus status,
                Pageable pageable
        );

        Page<Payment> findByAccountAccountNumberAndType(
                String accountNumber,
                PaymentType type,
                Pageable pageable
        );

        Page<Payment> findByStatus(
                PaymentStatus status,
                Pageable pageable
        );

        Page<Payment> findByType(
                PaymentType type,
                Pageable pageable
        );

}
