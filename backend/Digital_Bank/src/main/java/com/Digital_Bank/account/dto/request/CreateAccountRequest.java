package com.Digital_Bank.account.dto.request;

import com.Digital_Bank.account.domain.enums.AccountType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateAccountRequest(


@NotNull(message = "Cliente é obrigatório")
UUID customerId,

@NotNull(message = "Tipo da conta é obrigatório")
AccountType type

) {
        }