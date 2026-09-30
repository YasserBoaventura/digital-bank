package com.Digital_Bank.costumer.shared.response;

import java.util.*;
import  java.time.*;

public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}

