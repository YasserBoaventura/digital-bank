package com.Digital_Bank.card.repository;

import com.Digital_Bank.card.domain.entity.Card;
import com.Digital_Bank.card.domain.enums.CardStatus;
import com.Digital_Bank.card.domain.enums.CardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository <Card, UUID>{


        Optional<Card> findByReference(String reference);

        Page<Card> findByAccountAccountNumber(
                String accountNumber,
                Pageable pageable
        );

        Page<Card> findByAccountAccountNumberAndStatus(
                String accountNumber,
                CardStatus status,
                Pageable pageable
        );

        Page<Card> findByStatus(
                CardStatus status,
                Pageable pageable
        );

        Page<Card> findByAccountAccountNumberAndType(
                String accountNumber,
                CardType type,
                Pageable pageable
        );


}

