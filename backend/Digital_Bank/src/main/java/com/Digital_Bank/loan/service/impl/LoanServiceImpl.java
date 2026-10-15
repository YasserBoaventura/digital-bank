package com.Digital_Bank.loan.service.impl;

import com.Digital_Bank.loan.service.LoanService;

import com.Digital_Bank.account.domain.entity.Account;
import com.Digital_Bank.account.domain.enums.AccountStatus;
import com.Digital_Bank.account.repository.AccountRepository;
import com.Digital_Bank.loan.domain.entity.Loan;
import com.Digital_Bank.loan.domain.enums.LoanStatus;
import com.Digital_Bank.loan.domain.enums.LoanType;
import com.Digital_Bank.loan.dto.request.CreateLoanRequest;
import com.Digital_Bank.loan.dto.request.ReviewLoanRequest;
import com.Digital_Bank.loan.dto.response.LoanResponse;
import com.Digital_Bank.loan.repository.LoanRepository;
import com.Digital_Bank.loan.shared.exception.LoanNotFoundException;
import com.Digital_Bank.loan.shared.exception.LoanProcessingException;
import com.Digital_Bank.transaction.domain.emuns.TransactionStatus;
import com.Digital_Bank.transaction.domain.emuns.TransactionType;
import com.Digital_Bank.transaction.domain.entity.Transaction;

import com.Digital_Bank.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoanServiceImpl  implements LoanService {

    private final LoanRepository loanRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private static final BigDecimal MAX_LOAN_AMOUNT =
            new BigDecimal("1000000.00");

    @Override
    @Transactional
    public LoanResponse create(CreateLoanRequest request) {
        Account account = accountRepository
                .findByAccountNumber(request.accountNumber())
                .orElseThrow(() -> new LoanProcessingException(
                        "Account not found: " + request.accountNumber()
                ));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new LoanProcessingException(
                    "Cannot request a loan for an inactive account"
            );
        }

        if (request.requestedAmount().compareTo(MAX_LOAN_AMOUNT) > 0) {
            throw new LoanProcessingException(
                    "Requested amount exceeds the maximum loan amount"
            );
        }

        Loan loan = Loan.builder()
                .account(account)
                .type(request.type())
                .status(LoanStatus.PENDING)
                .requestedAmount(request.requestedAmount()
                        .setScale(2, RoundingMode.HALF_UP))
                .outstandingBalance(BigDecimal.ZERO.setScale(2))
                .termMonths(request.termMonths())
                .purpose(request.purpose().trim())
                .build();

            return mapToResponse(loanRepository.save(loan));
        }

        @Override
        public LoanResponse findById(UUID id) {
            return mapToResponse(getLoan(id));
        }

        @Override
        public LoanResponse findByReference(String reference) {
            Loan loan = loanRepository.findByReference(reference)
                    .orElseThrow(() -> new LoanNotFoundException(
                            "Loan not found with reference: " + reference
                    ));

            return mapToResponse(loan);
        }

        @Override
        public Page<LoanResponse> findByAccountNumber(
                String accountNumber, Pageable pageable
        ) {
            return requireResults(
                    loanRepository.findByAccountAccountNumber(accountNumber, pageable),
                    "No loans found for account number: " + accountNumber
            );
        }

        @Override
        public Page<LoanResponse> findByAccountNumberAndStatus(
                String accountNumber, LoanStatus status, Pageable pageable
    ) {
        return requireResults(
                loanRepository.findByAccountAccountNumberAndStatus(
                        accountNumber, status, pageable
                ),
                "No loans found for account " + accountNumber
                        + " with status " + status
        );
    }

        @Override
        public Page<LoanResponse> findByAccountNumberAndType(
                String accountNumber, LoanType type, Pageable pageable
        ) {
        return requireResults(
                loanRepository.findByAccountAccountNumberAndType(
                        accountNumber, type, pageable
                ),
                "No loans found for account " + accountNumber
                        + " with type " + type
        );
        }

        @Override
        public Page<LoanResponse> findByStatus(
                LoanStatus status, Pageable pageable
        ) {
            return requireResults(
                    loanRepository.findByStatus(status, pageable),
                    "No loans found with status: " + status
            );
        }

        @Override
        public Page<LoanResponse> findByType(
                LoanType type, Pageable pageable
        ) {
        return requireResults(
                loanRepository.findByType(type, pageable),
                "No loans found with type: " + type
        );
    }

        @Override
        @Transactional
        public LoanResponse review(UUID id, ReviewLoanRequest request) {
            Loan loan = getLoan(id);

        if (loan.getStatus() != LoanStatus.PENDING
                && loan.getStatus() != LoanStatus.UNDER_REVIEW) {
            throw new LoanProcessingException(
                    "Only pending or under-review loans can be reviewed"
            );
        }
        if (Boolean.TRUE.equals(request.approved())) {
            if (request.annualInterestRate() == null) {
                throw new LoanProcessingException(
                        "Interest rate is required for approval"
                );
            }

            BigDecimal rate = request.annualInterestRate();

            BigDecimal interest = loan.getRequestedAmount()
                    .multiply(rate)
                    .multiply(BigDecimal.valueOf(loan.getTermMonths()))
                    .divide(new BigDecimal("1200"), 2, RoundingMode.HALF_UP);

            BigDecimal total = loan.getRequestedAmount()
                    .add(interest)
                    .setScale(2, RoundingMode.HALF_UP);

            loan.setApprovedAmount(loan.getRequestedAmount());
            loan.setInterestRate(rate);
            loan.setTotalRepayable(total);
            loan.setOutstandingBalance(total);
            loan.setStatus(LoanStatus.APPROVED);
            loan.setApprovedAt(LocalDateTime.now());
            loan.setDueDate(LocalDate.now().plusMonths(loan.getTermMonths()));
            loan.setRejectionReason(null);

        } else {
            if (request.rejectionReason() == null
                    || request.rejectionReason().isBlank()) {
                throw new LoanProcessingException(
                        "Rejection reason is required"
                );
            }

            loan.setStatus(LoanStatus.REJECTED);
            loan.setRejectionReason(request.rejectionReason().trim());
            loan.setApprovedAmount(null);
            loan.setInterestRate(null);
            loan.setTotalRepayable(null);
            loan.setOutstandingBalance(BigDecimal.ZERO.setScale(2));
        }

        return mapToResponse(loanRepository.save(loan));
    }

    @Override
    @Transactional
    public LoanResponse disburse(UUID id) {
        Loan loan = getLoan(id);

        if (loan.getStatus() != LoanStatus.APPROVED) {
            throw new LoanProcessingException(
                    "Only approved loans can be disbursed"
            );
        }

        Account account = accountRepository
                .findByAccountNumberForUpdate(
                        loan.getAccount().getAccountNumber()
                )
                .orElseThrow(() -> new LoanProcessingException(
                        "Account not found for loan disbursement"
                ));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new LoanProcessingException(
                    "Cannot disburse a loan to an inactive account"
            );
        }

        BigDecimal amount = loan.getApprovedAmount();
        BigDecimal balanceBefore = account.getBalance();
        BigDecimal balanceAfter = balanceBefore.add(amount);

        account.setBalance(balanceAfter);
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .account(account)
                .type(TransactionType.DEPOSIT)
                .status(TransactionStatus.COMPLETED)
                .amount(amount)
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .description("Loan disbursement: " + loan.getReference())
                    .build();

            transactionRepository.save(transaction);

            loan.setStatus(LoanStatus.DISBURSED);
            loan.setDisbursedAt(LocalDateTime.now());

            return mapToResponse(loanRepository.save(loan));
        }

    @Override
    @Transactional
    public LoanResponse repay(UUID id, BigDecimal amount) {
        Loan loan = getLoan(id);

        if (loan.getStatus() != LoanStatus.DISBURSED) {
            throw new LoanProcessingException(
                    "Only disbursed loans can receive repayments"
            );
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new LoanProcessingException(
                    "Repayment amount must be greater than zero"
            );
        }

        BigDecimal payment = amount.setScale(2, RoundingMode.HALF_UP);

        if (payment.compareTo(loan.getOutstandingBalance()) > 0) {
            throw new LoanProcessingException(
                    "Repayment exceeds the outstanding loan balance"
            );
        }

        Account account = accountRepository
                .findByAccountNumberForUpdate(
                        loan.getAccount().getAccountNumber()
                )
                .orElseThrow(() -> new LoanProcessingException(
                        "Account not found for loan repayment"
                ));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new LoanProcessingException(
                    "Cannot repay a loan from an inactive account"
            );
        }

    BigDecimal balanceBefore = account.getBalance();

    if (balanceBefore.compareTo(payment) < 0) {
        throw new LoanProcessingException(
                "Insufficient account balance for loan repayment"
            );
        }

        BigDecimal balanceAfter = balanceBefore.subtract(payment);

        account.setBalance(balanceAfter);
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .account(account)
                .type(TransactionType.WITHDRAWAL)
                .status(TransactionStatus.COMPLETED)
                .amount(payment)
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .description("Loan repayment: " + loan.getReference())
                .build();

        transactionRepository.save(transaction);

        BigDecimal outstanding = loan.getOutstandingBalance()
                .subtract(payment)
                .setScale(2, RoundingMode.HALF_UP);

        loan.setOutstandingBalance(outstanding);

        if (outstanding.compareTo(BigDecimal.ZERO) == 0) {
            loan.setStatus(LoanStatus.REPAID);
        }

        return mapToResponse(loanRepository.save(loan));
    }

    @Override
    @Transactional
    public LoanResponse cancel(UUID id) {
        Loan loan = getLoan(id);

        if (loan.getStatus() != LoanStatus.PENDING
                && loan.getStatus() != LoanStatus.UNDER_REVIEW
                && loan.getStatus() != LoanStatus.APPROVED) {
            throw new LoanProcessingException(
                    "This loan cannot be cancelled in its current status"
            );
        }

        loan.setStatus(LoanStatus.CANCELLED);

        return mapToResponse(loanRepository.save(loan));
    }

    private Loan getLoan(UUID id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException(
                        "Loan not found with id: " + id
                ));
    }

    private Page<LoanResponse> requireResults(
            Page<Loan> loans, String message
    ) {
        if (loans.isEmpty()) {
            throw new LoanNotFoundException(message);
        }

        return loans.map(this::mapToResponse);
    }

    private LoanResponse mapToResponse(Loan loan) {
        return new LoanResponse(
                loan.getId(),
                loan.getAccount().getAccountNumber(),
                loan.getReference(),
                loan.getType(),
                loan.getStatus(),
                loan.getRequestedAmount(),
                loan.getApprovedAmount(),
                loan.getInterestRate(),
                loan.getTermMonths(),
                loan.getTotalRepayable(),
                loan.getOutstandingBalance(),
                loan.getPurpose(),
                loan.getRejectionReason(),
                loan.getRequestedAt(),
                loan.getApprovedAt(),
                loan.getDisbursedAt(),
                loan.getDueDate(),
                loan.getCreatedAt(),
                loan.getUpdatedAt()
        );
    }




}
