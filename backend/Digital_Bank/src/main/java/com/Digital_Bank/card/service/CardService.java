package com.Digital_Bank.card.service;
import com.Digital_Bank.card.domain.enums.CardStatus;
import com.Digital_Bank.card.domain.enums.CardType;
import com.Digital_Bank.card.dto.request.CreateCardRequest;
import com.Digital_Bank.card.dto.response.CardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CardService {


        CardResponse create(CreateCardRequest request);

        CardResponse findById(UUID id);

        CardResponse findByReference(String reference);

        Page<CardResponse> findByAccountNumber(
                String accountNumber,
                Pageable pageable
        );

        Page<CardResponse> findByAccountNumberAndStatus(
                String accountNumber,
                CardStatus status,
                Pageable pageable
        );

        Page<CardResponse> findByAccountNumberAndType(
                String accountNumber,
                CardType type,
                Pageable pageable
        );

        Page<CardResponse> findByStatus(
                CardStatus status,
                Pageable pageable
        );

        CardResponse block(UUID id);

        CardResponse unblock(UUID id);

        CardResponse cancel(UUID id);


}
