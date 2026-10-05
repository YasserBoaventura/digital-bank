package com.Digital_Bank.transfer.domain.entity;


import com.Digital_Bank.account.domain.entity.Account;
import com.Digital_Bank.transfer.domain.enums.TransferStatus;
import com.Digital_Bank.transfer.domain.enums.TransferType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "transfers",
        indexes = {
                @Index(name = "idx_transfer_reference", columnList = "reference"),
                @Index(name = "idx_transfer_sender", columnList = "sender_account_id"),
                @Index(name = "idx_transfer_receiver", columnList = "receiver_account_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transfer {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(
                name = "sender_account_id",
                nullable = false
        )
        private Account senderAccount;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(
                name = "receiver_account_id",
                nullable = false
        )
        private Account receiverAccount;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private TransferType type;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private TransferStatus status;

        @Column(nullable = false, precision = 19, scale = 2)
        private BigDecimal amount;

        @Column(nullable = false, unique = true, length = 50)
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
                reference = "TRF-" + UUID.randomUUID()
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
