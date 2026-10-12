package com.Digital_Bank.payment.domain.entity;


import com.Digital_Bank.account.domain.entity.Account;
import com.Digital_Bank.payment.domain.enums.PaymentStatus;
import com.Digital_Bank.payment.domain.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "payments",
        indexes = {
                @Index(
                        name = "idx_payment_reference",
                        columnList = "reference"
                ),
                @Index(
                        name = "idx_payment_account",
                        columnList = "account_id"
                ),
                @Index(
                        name = "idx_payment_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_payment_type",
                        columnList = "type"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

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
        private PaymentType type;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private PaymentStatus status;

        @Column(
                nullable = false,
                precision = 19,
                scale = 2
        )
        private BigDecimal amount;

        @Column(
                nullable = false,
                unique = true,
                length = 50
        )
        private String reference;

        @Column(length = 255)
        private String description;

        @Column(nullable = false)
        private LocalDateTime createdAt;

        @Column(nullable = false)
        private LocalDateTime updatedAt;

        @PrePersist
        protected void onCreate() {

            LocalDateTime now = LocalDateTime.now();

            createdAt = now;
            updatedAt = now;

            if (reference == null || reference.isBlank()) {
                reference = "PAY-" +
                        UUID.randomUUID()
                                .toString()
                                .replace("-", "")
                                .substring(0, 16)
                                .toUpperCase();
            }
        }

        @PreUpdate
        protected void onUpdate() {
            updatedAt = LocalDateTime.now();
        }

}
