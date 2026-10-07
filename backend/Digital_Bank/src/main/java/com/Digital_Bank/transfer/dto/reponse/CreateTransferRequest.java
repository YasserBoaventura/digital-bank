package com.Digital_Bank.transfer.dto.reponse;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransferRequest (

        @NotBlank(message = "Sender account number is required")
        String senderAccountNumber,

            @NotBlank(message = "Receiver account number is required")
            String receiverAccountNumber,

    @NotNull(message = "Amount is required")
    @DecimalMin(
            value = "0.01",
            message = "Transfer amount must be greater than zero"
    )
    BigDecimal amount,

    @Size(
            max = 255,
            message = "Description cannot exceed 255 characters"
    )
    String description
) {

}