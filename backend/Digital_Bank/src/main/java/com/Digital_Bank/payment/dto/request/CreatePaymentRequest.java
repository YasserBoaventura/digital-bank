package com.Digital_Bank.payment.dto.request;
import java.math.BigDecimal;
import java.util.*;

import com.Digital_Bank.payment.domain.enums.PaymentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreatePaymentRequest (
        @NotBlank(message = "Account number is required")
        String accountNumber,

        @NotNull(message = "Payment type is required")
        PaymentType type,

        @NotNull(message = "Amount is required")
        @DecimalMin(
                value = "0.01",
                message = "Payment amount must be greater than zero"
        )
        BigDecimal amount,

        @Size(
                max = 255,
                message = "Description must not exceed 255 characters"
        )
        String description
){

}
