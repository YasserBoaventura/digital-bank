package com.Digital_Bank.audit.repository;

import com.Digital_Bank.audit.domain.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AudiRepository extends JpaRepository<AuditLog, UUID>  {



        Page<AuditLog> findAllByOrderByCreatedAtDesc(
                Pageable pageable
        );

        Page<AuditLog> findByActorIdOrderByCreatedAtDesc(
                UUID actorId,
                Pageable pageable
        );

        Page<AuditLog>
        findByEntityTypeAndEntityIdOrderByCreatedAtDesc(
                String entityType,
                String entityId,
                Pageable pageable
        );

}
