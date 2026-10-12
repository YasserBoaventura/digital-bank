package com.Digital_Bank.payment.service;

import com.Digital_Bank.payment.domain.enums.PaymentStatus;
import com.Digital_Bank.payment.domain.enums.PaymentType;
import com.Digital_Bank.payment.dto.reponse.PaymentResponse;
import com.Digital_Bank.payment.dto.request.CreatePaymentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PaymentService {

        PaymentResponse create(CreatePaymentRequest request);

        PaymentResponse findById(UUID id);

        PaymentResponse findByReference(String reference);

        Page<PaymentResponse> findByAccountNumber(
                String accountNumber,
                Pageable pageable
        );

        Page<PaymentResponse> findByAccountNumberAndStatus(
                String accountNumber,
                PaymentStatus status,
                Pageable pageable
        );

        Page<PaymentResponse> findByAccountNumberAndType(
                String accountNumber,
                PaymentType type,
                Pageable pageable
        );

        Page<PaymentResponse> findByStatus(
                PaymentStatus status,
                Pageable pageable
        );

        Page<PaymentResponse> findByType(
                PaymentType type,
                Pageable pageable
        );

}
