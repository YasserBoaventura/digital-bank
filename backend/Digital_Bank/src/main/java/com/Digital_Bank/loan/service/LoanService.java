package com.Digital_Bank.loan.service;




import com.Digital_Bank.loan.domain.enums.LoanStatus;
import com.Digital_Bank.loan.domain.enums.LoanType;

import com.Digital_Bank.loan.dto.request.CreateLoanRequest;
import com.Digital_Bank.loan.dto.request.ReviewLoanRequest;
import com.Digital_Bank.loan.dto.response.LoanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

public interface LoanService {



        LoanResponse create(CreateLoanRequest request);

        LoanResponse findById(UUID id);

        LoanResponse findByReference(String reference);

        Page<LoanResponse> findByAccountNumber(
                String accountNumber, Pageable pageable
        );

        Page<LoanResponse> findByAccountNumberAndStatus(
                String accountNumber, LoanStatus status, Pageable pageable
        );

        Page<LoanResponse> findByAccountNumberAndType(
                String accountNumber, LoanType type, Pageable pageable
        );

        Page<LoanResponse> findByStatus(
                LoanStatus status, Pageable pageable
        );

        Page<LoanResponse> findByType(
                LoanType type, Pageable pageable
        );

        LoanResponse review(UUID id, ReviewLoanRequest request);

        LoanResponse disburse(UUID id);

        LoanResponse repay(UUID id, BigDecimal amount);

        LoanResponse cancel(UUID id);

}
