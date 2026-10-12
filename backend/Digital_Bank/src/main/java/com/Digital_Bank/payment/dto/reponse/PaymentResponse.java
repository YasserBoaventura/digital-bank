package com.Digital_Bank.payment.dto.reponse;

import com.Digital_Bank.payment.domain.enums.PaymentStatus;
import com.Digital_Bank.payment.domain.enums.PaymentType;

import java.math.*;
import java.time.*;
import java.util.UUID;

public record PaymentResponse(
        UUID id,

        String accountNumber,

        PaymentType type,

        PaymentStatus status,

        BigDecimal amount,

        String reference,

        String description,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
