package com.Digital_Bank.audit.service;

import com.Digital_Bank.audit.domain.enums.AuditAction;
import com.Digital_Bank.audit.dto.AuditLogResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface AuditService {

        void record(
                UUID actorId,
                AuditAction action,
                String entityType,
                String entityId,
                String description
        );

        Page<AuditLogResponse> findAll(int page, int size);

        Page<AuditLogResponse> findByActorId(
                UUID actorId,
                int page,
                int size
        );

        Page<AuditLogResponse> findByEntity(
                String entityType,
                String entityId,
                int page,
                int size
        );

}
