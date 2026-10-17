package com.Digital_Bank.savings.dto.response;


import com.Digital_Bank.savings.domain.enums.SavingsStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


public record SavingsResponse(

            UUID id,
            String reference,
            String accountNumber,
            SavingsStatus status,
            BigDecimal balance,
            BigDecimal annualInterestRate,
            LocalDateTime openedAt,
            LocalDateTime closedAt,
            LocalDateTime lastInterestCalculationAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}

