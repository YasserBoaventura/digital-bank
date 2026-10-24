package com.Digital_Bank.notification.controller;

import com.Digital_Bank.notification.domain.enums.NotificationStatus;
import com.Digital_Bank.notification.dto.request.CreateNotificationRequest;
import com.Digital_Bank.notification.dto.response.NotificationResponse;
import com.Digital_Bank.notification.service.NotificationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;


@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Validated
public class NotificationController {

    private final NotificationService notificationService;

        // Create a notification
        @PostMapping
        public ResponseEntity<NotificationResponse> create(
                @Valid @RequestBody CreateNotificationRequest request) {

        NotificationResponse response =
                notificationService.createForCustomer(
                        request.customerId(),
                        request.type(),
                        request.title(),
                        request.message()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

// Get notifications for a customer
@GetMapping("/customer/{customerId}")
public ResponseEntity<Page<NotificationResponse>> getCustomerNotifications(
        @PathVariable UUID customerId,
        @RequestParam(required = false) NotificationStatus status,
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {

     Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Direction.DESC, "createdAt")
    );
    Page<NotificationResponse> notifications =
            notificationService.findByCustomer(
                    customerId,
                    status,
                    pageable
            );

        return ResponseEntity.ok(notifications);
    }

    // Get a specific notification
    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponse> getNotification(
            @PathVariable UUID notificationId,
            @RequestParam UUID customerId) {

        return ResponseEntity.ok(
                notificationService.findById(
                        customerId,
                        notificationId
                )
        );
    }// Count unread notifications
        @GetMapping("/customer/{customerId}/unread/count")
        public ResponseEntity<Long> countUnreadNotifications(
                @PathVariable UUID customerId) {

            return ResponseEntity.ok(
                    notificationService.countUnread(customerId)
            );
        }

    // Mark one notification as read
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> markAsRead(
            @PathVariable UUID notificationId,
            @RequestParam UUID customerId){

        return ResponseEntity.ok(
                notificationService.markAsRead(
                        customerId,
                        notificationId
                )
        );
    }

    // Mark all customer notifications as read
        @PatchMapping("/customer/{customerId}/read-all")
        public ResponseEntity<Void> markAllAsRead(
                @PathVariable UUID customerId) {

            notificationService.markAllAsRead(customerId);

            return ResponseEntity.noContent().build();
        }

        // Delete a notification
        @DeleteMapping("/{notificationId}")
        public ResponseEntity<Void> deleteNotification(
                @PathVariable UUID notificationId,
                @RequestParam UUID customerId) {

            notificationService.delete(
                    customerId,
                    notificationId
            );

            return ResponseEntity.noContent().build();
        }



}
