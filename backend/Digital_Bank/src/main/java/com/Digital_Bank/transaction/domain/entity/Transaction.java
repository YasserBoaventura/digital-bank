package com.Digital_Bank.transaction.domain.entity;


import com.Digital_Bank.account.domain.entity.Account;
import com.Digital_Bank.transaction.domain.emuns.TransactionStatus;
import com.Digital_Bank.transaction.domain.emuns.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import java.time.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
        name = "transactions",
        indexes = {
                @Index(
                        name = "idx_transaction_account",
                        columnList = "account_id"
                ),
                @Index(
                        name = "idx_transaction_reference",
                        columnList = "reference"
                ),
                @Index(
                        name = "idx_transaction_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_transaction_type",
                        columnList = "type"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction  implements Serializable {


        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(
                name = "account_id",
                nullable = false
        )
        private Account account;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private TransactionType type;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private TransactionStatus status;

        @Column(
                nullable = false,
                precision = 19,
                scale = 2
        )
        private BigDecimal amount;

        @Column(
                name = "balance_before",
                nullable = false,
                precision = 19,
                scale = 2
        )
        private BigDecimal balanceBefore;

        @Column(
                name = "balance_after",
                nullable = false,
                precision = 19,
                scale = 2
        )
        private BigDecimal balanceAfter;

        @Column(length = 500)
        private String description;

        @Column(
                nullable = false,
                unique = true,
                length = 100
        )
        private String reference;

        @Column(nullable = false, updatable = false)
        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        @PrePersist
        protected void onCreate() {

                createdAt = LocalDateTime.now();
                updatedAt = LocalDateTime.now();

                if (reference == null || reference.isBlank()) {
                        reference = generateReference();
                }
        }

        @PreUpdate
        protected void onUpdate() {

                updatedAt = LocalDateTime.now();
        }

        private String generateReference() {

                return "TXN-" +
                        UUID.randomUUID()
                                .toString()
                                .replace("-", "")
                                .substring(0, 20)
                                .toUpperCase();
        }

}