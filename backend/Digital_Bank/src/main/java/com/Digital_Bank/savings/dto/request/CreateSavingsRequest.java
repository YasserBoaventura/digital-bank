package com.Digital_Bank.savings.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;



public record CreateSavingsRequest(

        @NotBlank(message = "Account number is required")
                String accountNumber,

        @NotNull(message = "Annual interest rate is required")
        @DecimalMin(
                value = "0.00",
                message = "Interest rate cannot be negative"
        )
        @DecimalMax(
                value = "100.00",
                message = "Interest rate cannot exceed 100%"
        )
        @Digits(integer = 3, fraction = 4)
        BigDecimal annualInterestRate

) {}