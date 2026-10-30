package com.Digital_Bank.audit.dto;


import com.Digital_Bank.audit.domain.enums.AuditAction;

import java.time.LocalDateTime;
import java.util.UUID;
public record AuditLogResponse(

        UUID id,
        UUID actorId,
        AuditAction action,
        String entityType,
        String entityId,
        String description,
        LocalDateTime createdAt
) {
}
