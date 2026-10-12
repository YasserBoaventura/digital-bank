package com.Digital_Bank.card.dto.response;



import com.Digital_Bank.card.domain.enums.CardBrand;
import com.Digital_Bank.card.domain.enums.CardStatus;
import com.Digital_Bank.card.domain.enums.CardType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
public record CardResponse(

            UUID id,
            String accountNumber,
            String reference,
            String maskedNumber,
            String lastFour,
            CardType type,
            CardBrand brand,
            CardStatus status,
            LocalDate expiryDate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}

