package com.Digital_Bank.loan.dto.response;
import com.Digital_Bank.loan.domain.enums.LoanStatus;
import com.Digital_Bank.loan.domain.enums.LoanType;

import java.time.*;
import java.math.*;
import java.util.UUID;

public record LoanResponse(

           UUID id,
           String accountNumber,
           String reference,
           LoanType type,
           LoanStatus status,
           BigDecimal requestedAmount,
           BigDecimal approvedAmount,
           BigDecimal interestRate,
           Integer termMonths,
           BigDecimal totalRepayable,
           BigDecimal outstandingBalance,
           String purpose,
           String rejectionReason,
           LocalDateTime requestedAt,
           LocalDateTime approvedAt,
           LocalDateTime disbursedAt,
           LocalDate dueDate,
           LocalDateTime createdAt,
           LocalDateTime updatedAt
) {}