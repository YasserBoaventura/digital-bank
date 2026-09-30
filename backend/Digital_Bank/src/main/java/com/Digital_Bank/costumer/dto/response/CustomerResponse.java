package com.Digital_Bank.costumer.dto.response;


import com.Digital_Bank.costumer.domain.enums.CustomerStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String fullName,
        String email,
        String phone,
        String documentNumber,
        LocalDate dateOfBirth,
        CustomerStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}