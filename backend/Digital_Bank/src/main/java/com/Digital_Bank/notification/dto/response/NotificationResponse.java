package com.Digital_Bank.notification.dto.response;


import com.Digital_Bank.notification.domain.entity.Notification;
import com.Digital_Bank.notification.domain.enums.NotificationStatus;
import com.Digital_Bank.notification.domain.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;
public record NotificationResponse (

        UUID id,
        UUID customerId,
        NotificationType type,
        String title,
        String message,
        NotificationStatus status,
        LocalDateTime createdAt,
        LocalDateTime readAt
) {

    public static NotificationResponse fromEntity(
            Notification notification) {

        return new NotificationResponse(
                notification.getId(),
                notification.getCustomer().getId(),
                notification.getType(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getStatus(),
                notification.getCreatedAt(),
                notification.getReadAt()
        );
    }
}
