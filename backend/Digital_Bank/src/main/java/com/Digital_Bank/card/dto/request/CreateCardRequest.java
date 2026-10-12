package com.Digital_Bank.card.dto.request;
import com.Digital_Bank.card.domain.enums.CardBrand;
import com.Digital_Bank.card.domain.enums.CardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCardRequest(

            @NotBlank(message = "Account number is required")
            String accountNumber,

            @NotNull(message = "Card type is required")
            CardType type,

            @NotNull(message = "Card brand is required")
            CardBrand brand

    ) {}

