package com.Digital_Bank.savings.service;


import com.Digital_Bank.savings.dto.request.CreateSavingsRequest;
import com.Digital_Bank.savings.dto.response.SavingsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

public interface SavingsService {

        SavingsResponse create(CreateSavingsRequest request);

        SavingsResponse findById(UUID id);

        SavingsResponse findByReference(String reference);

        Page<SavingsResponse> findByAccountNumber(
                String accountNumber,
                Pageable pageable
        );

        SavingsResponse deposit(UUID id, BigDecimal amount);

        SavingsResponse withdraw(UUID id, BigDecimal amount);

        SavingsResponse calculateInterest(UUID id);

        SavingsResponse close(UUID id);
    }
