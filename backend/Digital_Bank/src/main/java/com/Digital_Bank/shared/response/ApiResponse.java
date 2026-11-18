package com.Digital_Bank.shared.response;

import java.time.*;

public record ApiResponse<T>(
            boolean success,
            String message,
            T data,
            LocalDateTime timestamp
    ) {

        public static <T> ApiResponse<T> success(
                String message,
                T data
        ) {
            return new ApiResponse<>(
                    true,
                    message,
                    data,
                    LocalDateTime.now()
            );
        }

        public static <T> ApiResponse<T> error(String message) {
            return new ApiResponse<>(
                    false,
                    message,
                    null,
                    LocalDateTime.now()
            );
        }


}

