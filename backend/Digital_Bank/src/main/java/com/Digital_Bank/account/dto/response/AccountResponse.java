package com.Digital_Bank.account.dto.response;

import com.Digital_Bank.account.domain.enums.AccountStatus;
import com.Digital_Bank.account.domain.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountResponse (

    UUID id,

    String accountNumber,

    AccountType type,

    AccountStatus status,

    BigDecimal balance,

    String currency,

    UUID customerId,

    LocalDateTime createdAt,

    LocalDateTime updatedAt

){
}
