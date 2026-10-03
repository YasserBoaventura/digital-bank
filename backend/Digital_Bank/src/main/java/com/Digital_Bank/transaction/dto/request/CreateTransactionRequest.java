package com.Digital_Bank.transaction.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransactionRequest(


        @NotNull(message = "Account ID is required")

        UUID accountId,

        @NotNull(message = "Amount is required")
        @DecimalMin(
                value = "0.01",
                message = "Amount must be greater than zero"
        )
        BigDecimal amount,

        @Size(
                max = 500,
                message = "Description cannot exceed 500 characters"
        )
        String description

) {
}

