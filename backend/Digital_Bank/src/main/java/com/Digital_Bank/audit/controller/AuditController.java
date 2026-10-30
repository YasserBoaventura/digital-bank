package com.Digital_Bank.audit.controller;

import com.Digital_Bank.audit.dto.AuditLogResponse;
import com.Digital_Bank.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;



@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

        @GetMapping
        public Page<AuditLogResponse> findAll(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "20") int size
        ) {
            return auditService.findAll(page, size);
        }

        @GetMapping("/actor/{actorId}")
        public Page<AuditLogResponse> findByActorId(
                @PathVariable UUID actorId,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "20") int size
        ) {
            return auditService.findByActorId(actorId, page, size);
        }

        @GetMapping("/entity")
        public Page<AuditLogResponse> findByEntity(
                @RequestParam String entityType,
                @RequestParam String entityId,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "20") int size
        ) {
            return auditService.findByEntity(
                    entityType,
                    entityId,
                    page,
                    size
            );
        }

}
