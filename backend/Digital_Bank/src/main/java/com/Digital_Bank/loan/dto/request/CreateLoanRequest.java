package com.Digital_Bank.loan.dto.request;
import java.math.BigDecimal;

import com.Digital_Bank.loan.domain.enums.LoanType;
import jakarta.validation.constraints.*;

public record CreateLoanRequest(

        @NotBlank(message = "Account number is required")
        String accountNumber,

        @NotNull(message = "Loan type is required")
        LoanType type,

        @NotNull(message = "Requested amount is required")
        @DecimalMin(
                value = "100.00",
                message = "Minimum loan amount is 100"
        )
        @Digits(integer = 17, fraction = 2)
        BigDecimal requestedAmount,

        @NotNull(message = "Loan term is required")
        @Min(value = 1, message = "Minimum term is 1 month")
        @Max(value = 60, message = "Maximum term is 60 months")
        Integer termMonths,

        @NotBlank(message = "Loan purpose is required")
        @Size(max = 255, message = "Purpose cannot exceed 255 characters")
        String purpose

) {}