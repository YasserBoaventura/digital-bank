package com.Digital_Bank.card.domain.entity;


import com.Digital_Bank.account.domain.entity.Account;
import com.Digital_Bank.card.domain.enums.CardBrand;
import com.Digital_Bank.card.domain.enums.CardStatus;
import com.Digital_Bank.card.domain.enums.CardType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "cards",
        indexes = {
                @Index(name = "idx_card_account", columnList = "account_id"),
                @Index(name = "idx_card_status", columnList = "status"),
                @Index(name = "idx_card_reference", columnList = "reference")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_card_reference",
                        columnNames = "reference"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "account_id", nullable = false)
        private Account account;

        @Column(nullable = false, unique = true, length = 50)
        private String reference;

        @Column(name = "masked_number", nullable = false, length = 30)
        private String maskedNumber;

        @Column(name = "last_four", nullable = false, length = 4)
        private String lastFour;

        @Enumerated(EnumType.STRING)
        @Column(name = "card_type", nullable = false, length = 20)
        private CardType type;

        @Enumerated(EnumType.STRING)
        @Column(name = "card_brand", nullable = false, length = 20)
        private CardBrand brand;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 20)
        private CardStatus status;

        @Column(name = "expiry_date", nullable = false)
        private LocalDate expiryDate;

        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at", nullable = false)
        private LocalDateTime updatedAt;

        @PrePersist
        protected void onCreate() {
            if (reference == null || reference.isBlank()) {
                reference = "CRD-" + UUID.randomUUID()
                        .toString().toUpperCase();
            }

            if (createdAt == null) {
                createdAt = LocalDateTime.now();
            }

            updatedAt = LocalDateTime.now();
        }

        @PreUpdate
        protected void onUpdate() {
            updatedAt = LocalDateTime.now();
        }

}
