package com.Digital_Bank.payment.service.impl;

import com.Digital_Bank.payment.dto.reponse.PaymentResponse;
import com.Digital_Bank.payment.repository.PaymentRepository;
import com.Digital_Bank.payment.service.PaymentService;
import com.Digital_Bank.payment.shared.exception.PaymentNotFoundException;
import com.Digital_Bank.payment.shared.exception.PaymentProcessingException;
import com.Digital_Bank.transaction.domain.emuns.TransactionStatus;
import com.Digital_Bank.transaction.domain.emuns.TransactionType;
import com.Digital_Bank.transaction.domain.entity.Transaction;
import com.Digital_Bank.transaction.repository.TransactionRepository;
import com.Digital_Bank.transaction.shared.exception.InsufficientBalanceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Digital_Bank.account.domain.entity.Account;
import com.Digital_Bank.account.domain.enums.AccountStatus;
import com.Digital_Bank.account.repository.AccountRepository;
import com.Digital_Bank.payment.domain.entity.Payment;
import com.Digital_Bank.payment.domain.enums.PaymentStatus;
import com.Digital_Bank.payment.domain.enums.PaymentType;
import com.Digital_Bank.payment.dto.request.CreatePaymentRequest;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {


        private final PaymentRepository paymentRepository;
        private final AccountRepository accountRepository;
        private final TransactionRepository transactionRepository;

        @Override
        @Transactional
        public PaymentResponse create(
                CreatePaymentRequest request) {
            Account account =
                    findAccountForUpdate(
                            request.accountNumber()
                    );

            validateAccount(account);

            validateAmount(request.amount());

            BigDecimal balanceBefore =
                    account.getBalance();

            if (balanceBefore.compareTo(request.amount()) < 0) {
                throw new InsufficientBalanceException(
                        "Insufficient account balance"
                );
            }

            BigDecimal balanceAfter =
                    balanceBefore.subtract(
                            request.amount()
                    );

            account.setBalance(balanceAfter);

            accountRepository.save(account);

            Payment payment = Payment.builder()
                    .account(account)
                    .type(request.type())
                    .status(PaymentStatus.COMPLETED)
                    .amount(request.amount())
                    .description(request.description())
                    .build();

            Payment savedPayment =
                    paymentRepository.save(payment);

            Transaction transaction = Transaction.builder()
                    .account(account)
                    .type(TransactionType.PAYMENT)
                    .status(TransactionStatus.COMPLETED)
                    .amount(request.amount())
                    .balanceBefore(balanceBefore)
                    .balanceAfter(balanceAfter)
                    .description(
                            request.description()
                    )
                    .build();

            transactionRepository.save(transaction);

            return mapToResponse(savedPayment);
        }

        @Override
        @Transactional(readOnly = true)
        public PaymentResponse findById(UUID id) {

            Payment payment =
                paymentRepository.findById(id)
                        .orElseThrow(() ->
                                new PaymentNotFoundException(
                                        "Payment not found"
                                )
                        );

            return mapToResponse(payment);
        }

        @Override
        @Transactional(readOnly = true)
        public PaymentResponse findByReference(
                String reference
        ) {

            Payment payment =
                    paymentRepository
                            .findByReference(reference)
                            .orElseThrow(() ->
                                    new PaymentNotFoundException(
                                            "Payment not found"
                                    )
                            );

            return mapToResponse(payment);
        }
    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponse> findByAccountNumber(
            String accountNumber,
            Pageable pageable
    ) {

        Page<PaymentResponse> payments = paymentRepository
                .findByAccountAccountNumber(
                        accountNumber,
                        pageable
                )
                .map(this::mapToResponse);

        if (payments.isEmpty()) {
            throw new PaymentNotFoundException(
                    "No payments found for account number: "
                            + accountNumber
            );
        }

        return payments;
    }

        @Override
        @Transactional(readOnly = true)
        public Page<PaymentResponse>
        findByAccountNumberAndStatus(
                String accountNumber,
                PaymentStatus status,
                Pageable pageable) {

            Page<PaymentResponse> payments = paymentRepository
                    .findByAccountAccountNumberAndStatus(
                            accountNumber,
                            status,
                            pageable
                    )
                    .map(this::mapToResponse);
            if (payments.isEmpty()) {
                throw new PaymentNotFoundException(
                        "No payments found for account number: "
                                + accountNumber
                );
            }
                return  payments;
        }

        @Override
        @Transactional(readOnly = true)
        public Page<PaymentResponse>
        findByAccountNumberAndType(
                String accountNumber,
                PaymentType type,
                Pageable pageable
        ) {

            Page<PaymentResponse> payments = paymentRepository
                    .findByAccountAccountNumberAndType(
                            accountNumber,
                            type,
                            pageable
                    )
                    .map(this::mapToResponse);

              if(payments.isEmpty()){
                throw new PaymentNotFoundException(
                        "No payments found for account number: "
                                + accountNumber);
            }

            return payments;
        }

        @Override
        @Transactional(readOnly = true)
        public Page<PaymentResponse> findByStatus(
                PaymentStatus status,
                Pageable pageable
        ) {

            return paymentRepository
                    .findByStatus(status, pageable)
                    .map(this::mapToResponse);
        }

        @Override
        @Transactional(readOnly = true)
        public Page<PaymentResponse> findByType(
                PaymentType type,
                Pageable pageable
        ) {

            Page<PaymentResponse>  payments = paymentRepository
                    .findByType(type, pageable)
                    .map(this::mapToResponse);

            if(payments.isEmpty()){
                throw new PaymentNotFoundException(
                        "No payments found for account type: "
                                + type);
            }

            return payments;
        }

        private Account findAccountForUpdate(
                String accountNumber
        ) {

            return accountRepository
                    .findByAccountNumberForUpdate(
                            accountNumber
                    )
                    .orElseThrow(() ->
                            new PaymentProcessingException(
                                    "Account not found"
                            )
                    );
        }

        private void validateAccount(
                Account account
        ) {

            if (account.getStatus() != AccountStatus.ACTIVE) {
                throw new PaymentProcessingException(
                        "Account is not active"
                );
            }
        }

        private void validateAmount(
                BigDecimal amount
        ) {

            if (amount == null ||
                    amount.compareTo(BigDecimal.ZERO) <= 0) {

                throw new PaymentProcessingException(
                        "Payment amount must be greater than zero"
                );
            }
        }

        private PaymentResponse mapToResponse(
                Payment payment
        ) {

            return new PaymentResponse(
                    payment.getId(),
                    payment.getAccount().getAccountNumber(),
                    payment.getType(),
                    payment.getStatus(),
                    payment.getAmount(),
                    payment.getReference(),
                    payment.getDescription(),
                    payment.getCreatedAt(),
                    payment.getUpdatedAt()
            );
        }



}
