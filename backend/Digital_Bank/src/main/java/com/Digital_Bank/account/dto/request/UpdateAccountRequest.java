package com.Digital_Bank.account.dto.request;


import com.Digital_Bank.account.domain.enums.AccountStatus;
import com.Digital_Bank.account.domain.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import  java.util.*;
public record UpdateAccountRequest(
            UUID id,

            String accountNumber,

            AccountType type,

            AccountStatus status,

            BigDecimal balance,

            String currency,

            UUID customerId,

            LocalDateTime createdAt,

            LocalDateTime updatedAt

    ) {

}
