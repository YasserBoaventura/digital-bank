package com.Digital_Bank.transfer.dto.request;

import com.Digital_Bank.transfer.domain.enums.TransferStatus;
import com.Digital_Bank.transfer.domain.enums.TransferType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransferResponse(


        UUID id,

        UUID senderAccountId,

        UUID receiverAccountId,

        TransferType type,

        TransferStatus status,

        BigDecimal amount,

        String reference,

        String description,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
    ) {
    }


