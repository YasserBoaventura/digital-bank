package com.Digital_Bank.transfer.service;
import com.Digital_Bank.transfer.domain.enums.TransferStatus;
import com.Digital_Bank.transfer.dto.reponse.CreateTransferRequest;
import com.Digital_Bank.transfer.dto.request.TransferResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface TransferService {

    TransferResponse create(CreateTransferRequest request);

    TransferResponse findById(UUID id);

    TransferResponse findByReference(String reference);

    Page<TransferResponse> findBySenderAccount(  String accountNumber,
            Pageable pageable
    );

    Page<TransferResponse> findByReceiverAccount(
            String accountNumber,
            Pageable pageable
    );

    Page<TransferResponse> findByStatus(
            TransferStatus status,
            Pageable pageable
    );
    }


