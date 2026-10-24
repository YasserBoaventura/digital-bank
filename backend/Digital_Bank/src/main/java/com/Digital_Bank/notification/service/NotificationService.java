package com.Digital_Bank.notification.service;

import com.Digital_Bank.notification.domain.enums.NotificationStatus;
import com.Digital_Bank.notification.domain.enums.NotificationType;
import com.Digital_Bank.notification.dto.request.CreateNotificationRequest;
import com.Digital_Bank.notification.dto.response.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.*;
import java.util.UUID;

public interface NotificationService {

    NotificationResponse create(
            CreateNotificationRequest request);

    NotificationResponse createForCustomer(
            UUID customerId,
            NotificationType type,
            String title,
            String message
    );


    // Loan

    // Segurança e sistema
    NotificationResponse notifySecurityAlert(
            UUID customerId, String message
    );


    // Consulta e gestão
    NotificationResponse findById(
            UUID customerId, UUID notificationId
    );

    Page<NotificationResponse> findByCustomer(
            UUID customerId,
            NotificationStatus status,
            Pageable pageable
    );

    long countUnread(UUID customerId);

    NotificationResponse markAsRead(
            UUID customerId, UUID notificationId
    );

    void markAllAsRead(UUID customerId);

    void delete(UUID customerId, UUID notificationId);



}
