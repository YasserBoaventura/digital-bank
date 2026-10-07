package com.Digital_Bank.account.domain.entity;

import com.Digital_Bank.account.domain.enums.AccountStatus;
import com.Digital_Bank.account.domain.enums.AccountType;
import com.Digital_Bank.costumer.domain.entity.Customer;
import com.Digital_Bank.transaction.domain.entity.Transaction;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(
    name = "accounts",
    uniqueConstraints = {
            @UniqueConstraint(
                    name = "uk_account_number",
                    columnNames = "account_number"
            )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(
                name = "account_number",
                nullable = false,
                unique = true,
                length = 20
        )
        private String accountNumber;

        @Enumerated(EnumType.STRING)
        @Column(
                nullable = false,
                length = 20
        )
        private AccountType type;

        @Enumerated(EnumType.STRING)
        @Column(
                nullable = false,
                length = 20
        )
        private AccountStatus status;

        @Column(
                nullable = false,
                precision = 19,
                scale = 2
        )
        private BigDecimal balance;

        @Column(
                nullable = false,
                length = 3
        )
        private String currency;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(
                name = "customer_id",
                nullable = false
        )
        private Customer customer;

        @Column(name = "created_at", nullable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        @PrePersist
        protected void onCreate() {

            createdAt = LocalDateTime.now();

            if (status == null) {
                status = AccountStatus.ACTIVE;
            }

            if (balance == null) {
                balance = BigDecimal.ZERO;
            }

            if (currency == null) {
                currency = "MZN";
            }
        }
        //
        @OneToMany(
                mappedBy = "account",
                fetch = FetchType.LAZY
        )
        private List<Transaction> transactions = new ArrayList<>();

        @PreUpdate
        protected void onUpdate() {
            updatedAt = LocalDateTime.now();
        }

}
