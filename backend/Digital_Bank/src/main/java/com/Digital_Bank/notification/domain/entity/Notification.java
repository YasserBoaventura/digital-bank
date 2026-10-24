package com.Digital_Bank.notification.domain.entity;

import com.Digital_Bank.costumer.domain.entity.Customer;
import com.Digital_Bank.notification.domain.enums.NotificationStatus;
import com.Digital_Bank.notification.domain.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;




@Entity
@Table(
        name = "notifications",
    indexes = {
        @Index(
                name = "idx_notification_customer",
                columnList = "customer_id"
        ),
        @Index(
                name = "idx_notification_status",
                columnList = "status"
        ),
        @Index(
                name = "idx_notification_created_at",
                columnList = "created_at"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Notification {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "customer_id", nullable = false)
        private Customer customer;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 30)
        private NotificationType type;

        @Column(nullable = false, length = 120)
        private String title;

        @Column(nullable = false, length = 1000)
        private String message;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 20)
        @Builder.Default
        private NotificationStatus status = NotificationStatus.UNREAD;

        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "read_at")
        private LocalDateTime readAt;

        @PrePersist
        protected void onCreate() {
            if (createdAt == null) {
                createdAt = LocalDateTime.now();
            }

            if (status == null) {
                status = NotificationStatus.UNREAD;
            }
        }

}
