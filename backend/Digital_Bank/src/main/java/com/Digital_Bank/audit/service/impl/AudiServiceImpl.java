package com.Digital_Bank.audit.service.impl;


import com.Digital_Bank.audit.domain.entity.AuditLog;
import com.Digital_Bank.audit.domain.enums.AuditAction;
import com.Digital_Bank.audit.dto.AuditLogResponse;
import com.Digital_Bank.audit.repository.AudiRepository;
import com.Digital_Bank.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AudiServiceImpl  implements AuditService{

      private final AudiRepository auditRepository;

        @Override
        @Transactional
        public void record(
                UUID actorId,
                AuditAction action,
                String entityType,
                String entityId,
                String description
        ) {
            if (action == null) {
                throw new IllegalArgumentException(
                        "Audit action is required"
                );
            }

            if (entityType == null || entityType.isBlank()) {
                throw new IllegalArgumentException(
                        "Entity type is required"
                );
            }

            if (description == null || description.isBlank()) {
                throw new IllegalArgumentException(
                        "Audit description is required"
                );
            }

            AuditLog auditLog = new AuditLog();
            auditLog.setActorId(actorId);
            auditLog.setAction(action);
            auditLog.setEntityType(entityType.trim());
            auditLog.setEntityId(entityId);
            auditLog.setDescription(description.trim());

            auditRepository.save(auditLog);
        }

        @Override
        @Transactional(readOnly = true)
        public Page<AuditLogResponse> findAll(int page, int size) {
            validatePagination(page, size);

            return auditRepository
                    .findAllByOrderByCreatedAtDesc(
                            PageRequest.of(page, size)
                    )
                    .map(this::toResponse);
        }

        @Override
        @Transactional(readOnly = true)
        public Page<AuditLogResponse> findByActorId(
                UUID actorId,
                int page,
                int size
        ) {
            validatePagination(page, size);

            return auditRepository
                    .findByActorIdOrderByCreatedAtDesc(
                            actorId,
                            PageRequest.of(page, size)
                    )
                    .map(this::toResponse);
        }

        @Override
        @Transactional(readOnly = true)
        public Page<AuditLogResponse> findByEntity(
                String entityType,
                String entityId,
                int page,
                int size
        ) {
            validatePagination(page, size);

            if (entityType == null || entityType.isBlank()) {
                throw new IllegalArgumentException(
                        "Entity type is required"
                );
            }

            if (entityId == null || entityId.isBlank()) {
                throw new IllegalArgumentException(
                        "Entity ID is required"
                );
            }

            return auditRepository
                    .findByEntityTypeAndEntityIdOrderByCreatedAtDesc(
                            entityType.trim().toUpperCase(),
                            entityId.trim(),
                            PageRequest.of(page, size)
                    )
                    .map(this::toResponse);
        }

        private void validatePagination(int page, int size) {
            if (page < 0) {
                throw new IllegalArgumentException(
                        "Page must not be negative"
                );
            }

            if (size < 1 || size > 100) {
                throw new IllegalArgumentException(
                        "Page size must be between 1 and 100"
                );
            }
        }

        private AuditLogResponse toResponse(AuditLog auditLog) {
            return new AuditLogResponse(
                    auditLog.getId(),
                    auditLog.getActorId(),
                    auditLog.getAction(),
                    auditLog.getEntityType(),
                    auditLog.getEntityId(),
                    auditLog.getDescription(),
                    auditLog.getCreatedAt()
            );
        }
    }

