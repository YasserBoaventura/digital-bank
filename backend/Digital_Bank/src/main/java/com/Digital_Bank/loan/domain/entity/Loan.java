package com.Digital_Bank.loan.domain.entity;
import com.Digital_Bank.account.domain.entity.Account;
import com.Digital_Bank.loan.domain.enums.LoanStatus;
import com.Digital_Bank.loan.domain.enums.LoanType;
import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.math.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "loans",
    indexes = {
            @Index(name = "idx_loan_account", columnList = "account_id"),
            @Index(name = "idx_loan_status", columnList = "status"),
            @Index(name = "idx_loan_reference", columnList = "reference")
    },
    uniqueConstraints = {
            @UniqueConstraint(
                    name = "uk_loan_reference",
                    columnNames = "reference"
            )
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loan {


        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "account_id", nullable = false)
        private Account account;

        @Column(nullable = false, unique = true, length = 50)
        private String reference;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 30)
        private LoanType type;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 30)
        private LoanStatus status;

        @Column(name = "requested_amount", nullable = false, precision = 19, scale = 2)
        private BigDecimal requestedAmount;

        @Column(name = "approved_amount", precision = 19, scale = 2)
        private BigDecimal approvedAmount;

        @Column(name = "interest_rate", precision = 7, scale = 4)
        private BigDecimal interestRate;

        @Column(name = "term_months")
        private Integer termMonths;

        @Column(name = "total_repayable", precision = 19, scale = 2)
        private BigDecimal totalRepayable;

        @Column(name = "outstanding_balance", nullable = false, precision = 19, scale = 2)
        private BigDecimal outstandingBalance;

        @Column(nullable = false, length = 255)
        private String purpose;

        @Column(name = "rejection_reason", length = 500)
        private String rejectionReason;

        @Column(name = "requested_at", nullable = false, updatable = false)
        private LocalDateTime requestedAt;

        @Column(name = "approved_at")
        private LocalDateTime approvedAt;

        @Column(name = "disbursed_at")
        private LocalDateTime disbursedAt;

        @Column(name = "due_date")
        private LocalDate dueDate;

        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at", nullable = false)
        private LocalDateTime updatedAt;

        @Version
        private Long version;

        @PrePersist
        protected void onCreate() {
            if (reference == null || reference.isBlank()) {
                reference = "LOAN-" + UUID.randomUUID()
                        .toString().toUpperCase();
            }

            if (status == null) {
                status = LoanStatus.PENDING;
            }

            if (outstandingBalance == null) {
                outstandingBalance = BigDecimal.ZERO;
            }

            LocalDateTime now = LocalDateTime.now();

            if (requestedAt == null) {
                requestedAt = now;
            }

            if (createdAt == null) {
                createdAt = now;
            }

            updatedAt = now;
        }

        @PreUpdate
        protected void onUpdate() {
            updatedAt = LocalDateTime.now();
        }

}
