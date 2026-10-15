package com.Digital_Bank.loan.dto.request;



import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ReviewLoanRequest(


    @NotNull(message = "Approval decision is required")
    Boolean approved,

    @DecimalMin(value = "0.00", message = "Interest rate cannot be negative")
    @DecimalMax(value = "100.00", message = "Interest rate cannot exceed 100")
    @Digits(integer = 3, fraction = 4)
    BigDecimal annualInterestRate,

    @Size(max = 500, message = "Rejection reason cannot exceed 500 characters")
    String rejectionReason

    ) {}

