package com.Digital_Bank.costumer.domain.entity;


import com.Digital_Bank.costumer.domain.enums.CustomerStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(
        name = "customers",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_customer_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_customer_phone", columnNames = "phone"),
                @UniqueConstraint(name = "uk_customer_document", columnNames = "document_number")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Customer {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(name = "full_name", nullable = false, length = 150)
        private String fullName;

        @Column(nullable = false, unique = true, length = 150)
        private String email;

        @Column(nullable = false, unique = true, length = 30)
        private String phone;

        @Column(name = "document_number", nullable = false, unique = true, length = 50)
        private String documentNumber;

        @Column(name = "date_of_birth", nullable = false)
        private LocalDate dateOfBirth;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 30)
        private CustomerStatus status;

        @Column(name = "keycloak_user_id", unique = true)
        private String keycloakUserId;

        @Column(name = "created_at", nullable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        @PrePersist
        protected void onCreate() {
            createdAt = LocalDateTime.now();

            if (status == null) {
                status = CustomerStatus.PENDING_VERIFICATION;
            }
        }

        @PreUpdate
        protected void onUpdate() {
            updatedAt = LocalDateTime.now();
        }


    }


