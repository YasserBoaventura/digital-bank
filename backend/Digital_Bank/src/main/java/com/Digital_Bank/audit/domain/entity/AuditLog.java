package com.Digital_Bank.audit.domain.entity;


import com.Digital_Bank.audit.domain.enums.AuditAction;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.*;
import java.util.UUID;

@Entity
@Table(
name = "audit_logs",
    indexes = {
        @Index(name = "idx_audit_actor", columnList = "actor_id"),
        @Index(name = "idx_audit_entity", columnList = "entity_type, entity_id"),
        @Index(name = "idx_audit_created_at", columnList = "created_at")
    }
)
@Getter
@Setter
@NoArgsConstructor
public class AuditLog {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(name = "actor_id")
        private UUID actorId;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 50)
        private AuditAction action;

        @Column(name = "entity_type", nullable = false, length = 100)
        private String entityType;

        @Column(name = "entity_id", length = 100)
        private String entityId;

        @Column(nullable = false, length = 1000)
        private String description;

        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @PrePersist
        protected void onCreate() {
            if (createdAt == null) {
                createdAt = LocalDateTime.now();
            }
        }

}
