
package com.Digital_Bank.notification.controller;

import com.Digital_Bank.notification.domain.enums.NotificationStatus;
import com.Digital_Bank.notification.dto.request.CreateNotificationRequest;
import com.Digital_Bank.notification.dto.response.NotificationResponse;
import com.Digital_Bank.notification.service.NotificationService;
import com.Digital_Bank.shared.constants.ApiConstants;
import com.Digital_Bank.shared.response.ApiResponse;

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
@RequestMapping(ApiConstants.API_BASE_PATH + "/notifications")
@RequiredArgsConstructor
@Validated
public class NotificationController {

    private final NotificationService notificationService;

    // Create a notification
@PostMapping
public ResponseEntity<ApiResponse<NotificationResponse>> create(
        @Valid @RequestBody CreateNotificationRequest request
) {
    NotificationResponse response =
            notificationService.createForCustomer(
                    request.customerId(),
                    request.type(),
                    request.title(),
                    request.message()
            );

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(
                    "Notification created successfully",
                    response
            ));
}

    // Get notifications for a customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> getCustomerNotifications(
            @PathVariable UUID customerId,
            @RequestParam(required = false) NotificationStatus status,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<NotificationResponse> response =
                notificationService.findByCustomer(
                        customerId,
                        status,
                        pageable
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer notifications retrieved successfully",
                        response
                )
        );
    }

    // Get a specific notification
    @GetMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<NotificationResponse>> getNotification(
            @PathVariable UUID notificationId,
            @RequestParam UUID customerId
    ) {
        NotificationResponse response =
                notificationService.findById(
                        customerId,
                        notificationId
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Notification retrieved successfully",
                        response
                )
        );
    }

    // Count unread notifications
    @GetMapping("/customer/{customerId}/unread/count")
    public ResponseEntity<ApiResponse<Long>> countUnreadNotifications(
            @PathVariable UUID customerId
    ) {
        Long count = notificationService.countUnread(customerId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Unread notifications count retrieved successfully",
                        count
                )
        );
    }

    // Mark one notification as read
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(
            @PathVariable UUID notificationId,
            @RequestParam UUID customerId
    ) {
        NotificationResponse response =
                notificationService.markAsRead(
                        customerId,
                        notificationId
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Notification marked as read successfully",
                        response
                )
        );
    }

    // Mark all customer notifications as read
    @PatchMapping("/customer/{customerId}/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            @PathVariable UUID customerId
    ) {
        notificationService.markAllAsRead(customerId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "All customer notifications marked as read successfully",
                        null
                )
        );
    }

    // Delete a notification
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @PathVariable UUID notificationId,
            @RequestParam UUID customerId
    ) {
        notificationService.delete(
                customerId,
                notificationId
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Notification deleted successfully",
                        null
                )
        );
    }
}
