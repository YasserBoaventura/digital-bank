package com.Digital_Bank.transaction.shared.response;
import java.time.*;

public record ErrorResponse(   int status,
                               String error,
                               String message,
                               LocalDateTime timestamp) {
}
