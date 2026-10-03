package com.Digital_Bank.transaction.service;

import com.Digital_Bank.transaction.domain.emuns.TransactionStatus;
import com.Digital_Bank.transaction.domain.emuns.TransactionType;
import com.Digital_Bank.transaction.dto.request.CreateTransactionRequest;
import com.Digital_Bank.transaction.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TransactionService {


        TransactionResponse deposit(CreateTransactionRequest request);

        TransactionResponse withdraw(CreateTransactionRequest request);

        TransactionResponse findById(UUID id);

        TransactionResponse findByReference(String reference);

        Page<TransactionResponse> findByAccount(UUID accountId, Pageable pageable);

        Page<TransactionResponse> findByAccountAndType(UUID accountId, TransactionType type, Pageable pageable);

        Page<TransactionResponse> findByAccountAndStatus(UUID accountId, TransactionStatus status, Pageable pageable);

        Page<TransactionResponse> findByType(TransactionType type, Pageable pageable);

        Page<TransactionResponse> findByStatus(TransactionStatus status, Pageable pageable);
    }


