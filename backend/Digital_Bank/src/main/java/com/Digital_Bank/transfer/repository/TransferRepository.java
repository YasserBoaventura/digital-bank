package com.Digital_Bank.transfer.repository;


import com.Digital_Bank.transfer.domain.entity.Transfer;
import com.Digital_Bank.transfer.domain.enums.TransferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransferRepository  extends JpaRepository<Transfer, UUID>{



        Optional<Transfer> findByReference(String reference);

        boolean existsByReference(String reference);

        Page<Transfer> findBySenderAccountId(
                UUID senderAccountId,
                Pageable pageable
        );

        Page<Transfer> findByReceiverAccountId(
                UUID receiverAccountId,
                Pageable pageable
        );

        Page<Transfer> findByStatus(
                TransferStatus status,
                Pageable pageable
        );

}
