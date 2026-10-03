package com.Digital_Bank.transaction.dto.response;
import com.Digital_Bank.transaction.domain.emuns.TransactionStatus;
import com.Digital_Bank.transaction.domain.emuns.TransactionType;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

public record TransactionResponse (


        UUID id,

        UUID accountId,

        TransactionType type,

        TransactionStatus status,

        BigDecimal amount,

        BigDecimal balanceBefore,

        BigDecimal balanceAfter,

        String description,

        String reference,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
)
{

}
