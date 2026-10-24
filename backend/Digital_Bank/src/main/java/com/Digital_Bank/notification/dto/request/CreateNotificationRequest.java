package com.Digital_Bank.notification.dto.request;



import com.Digital_Bank.notification.domain.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
public record CreateNotificationRequest(

        @NotNull(message = "Customer ID is required")
                UUID customerId,

        @NotNull(message = "Notification type is required")
        NotificationType type,

        @NotBlank(message = "Title is required")
        @Size(max = 120, message = "Title cannot exceed 120 characters")
        String title,

        @NotBlank(message = "Message is required")
        @Size(max = 1000, message = "Message cannot exceed 1000 characters")
        String message

) {}
