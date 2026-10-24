package com.Digital_Bank.notification.repository;



import com.Digital_Bank.notification.domain.entity.Notification;
import com.Digital_Bank.notification.domain.enums.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID>  {


        Page<Notification> findByCustomer_Id(
                UUID customerId,
                Pageable pageable
        );

        Page<Notification> findByCustomer_IdAndStatus(
                UUID customerId,
                NotificationStatus status,
                Pageable pageable
        );

        Optional<Notification> findByIdAndCustomer_Id(
                UUID id,
                UUID customerId
        );

        long countByCustomer_IdAndStatus(
                UUID customerId,
                NotificationStatus status
        );

        @Modifying
        @Query("""
        UPDATE Notification n
        SET n.status = :status,
            n.readAt = :readAt
        WHERE n.customer.id = :customerId
          AND n.status = :unreadStatus
    """)
        int markAllAsRead(
                @Param("customerId") UUID customerId,
                @Param("status") NotificationStatus status,
                @Param("readAt") LocalDateTime readAt,
                @Param("unreadStatus") NotificationStatus unreadStatus
        );

}
