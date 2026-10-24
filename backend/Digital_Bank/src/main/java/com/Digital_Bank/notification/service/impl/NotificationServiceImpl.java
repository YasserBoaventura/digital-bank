package com.Digital_Bank.notification.service.impl;



import com.Digital_Bank.costumer.domain.entity.Customer;
import com.Digital_Bank.costumer.repository.CustomerRepository;
import com.Digital_Bank.notification.domain.entity.Notification;
import com.Digital_Bank.notification.domain.enums.NotificationStatus;
import com.Digital_Bank.notification.domain.enums.NotificationType;
import com.Digital_Bank.notification.dto.request.CreateNotificationRequest;
import com.Digital_Bank.notification.dto.response.NotificationResponse;
import com.Digital_Bank.notification.repository.NotificationRepository;
import com.Digital_Bank.notification.service.NotificationService;
import com.Digital_Bank.notification.shared.exception.NotificationNotFoundException;
import com.Digital_Bank.notification.shared.exception.NotificationProcessingException;
import  java.math.*;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationServiceImpl  implements  NotificationService{

    private final NotificationRepository notificationRepository;
    private final CustomerRepository customerRepository;


@Transactional
@Override
public NotificationResponse create(
        CreateNotificationRequest request) {

Customer customer = customerRepository
        .findById(request.customerId())
        .orElseThrow(() ->
                new NotificationProcessingException(
                        "Customer not found: " + request.customerId()
                )
        );

    Notification notification = Notification.builder()
            .customer(customer)
            .type(request.type())
            .title(request.title().trim())
            .message(request.message().trim())
            .status(NotificationStatus.UNREAD)
            .build();

    Notification saved = notificationRepository.save(notification);

    return NotificationResponse.fromEntity(saved);
}

    @Override
    public NotificationResponse findById(
            UUID customerId,
            UUID notificationId) {

        return NotificationResponse.fromEntity(
                findNotification(customerId, notificationId)
        );
    }

    @Override
    public Page<NotificationResponse> findByCustomer(
            UUID customerId,
            NotificationStatus status,
            Pageable pageable) {

        Page<Notification> notifications;

        if (status == null) {
            notifications = notificationRepository
                    .findByCustomer_Id(customerId, pageable);
        } else {
            notifications = notificationRepository
                    .findByCustomer_IdAndStatus(
                            customerId, status, pageable
                    );
        }

        return notifications.map(NotificationResponse::fromEntity);
    }

    @Override
    public long countUnread(UUID customerId) {
        return notificationRepository.countByCustomer_IdAndStatus(
                customerId,
                NotificationStatus.UNREAD
        );
    }

    @Override
    @Transactional
    public NotificationResponse markAsRead(
            UUID customerId,
            UUID notificationId) {

        Notification notification =
                findNotification(customerId, notificationId);

        if (notification.getStatus() == NotificationStatus.UNREAD) {
            notification.setStatus(NotificationStatus.READ);
            notification.setReadAt(LocalDateTime.now());
        }

        return NotificationResponse.fromEntity(
                notificationRepository.save(notification)
        );
    }

    @Override
    @Transactional
    public void markAllAsRead(UUID customerId) {

         notificationRepository.markAllAsRead(
                customerId,
                NotificationStatus.READ,
                LocalDateTime.now(),
                NotificationStatus.UNREAD
        );
    }

    @Override
    @Transactional
    public void delete(
            UUID customerId,
            UUID notificationId) {

        Notification notification =
                findNotification(customerId, notificationId);

        notificationRepository.delete(notification);
    }

    private Notification findNotification(
            UUID customerId,
            UUID notificationId) {

        return notificationRepository
                .findByIdAndCustomer_Id(notificationId, customerId)
            .orElseThrow(() ->
                    new NotificationNotFoundException(
                            "Notification not found: " + notificationId
                    )
            );
}

    @Override
    public NotificationResponse createForCustomer(
            UUID customerId,
            NotificationType type,
            String title,
            String message) {

        CreateNotificationRequest request =
                new CreateNotificationRequest(
                        customerId,
                        type,
                        title,
                        message
                );

        return create(request);
    }



// SECURITY AND SYSTEM

    @Override
    public NotificationResponse notifySecurityAlert(
            UUID customerId, String message) {

        return createForCustomer(
                customerId,
                NotificationType.SECURITY,
                "Security alert",
                message
        );
    }



}
