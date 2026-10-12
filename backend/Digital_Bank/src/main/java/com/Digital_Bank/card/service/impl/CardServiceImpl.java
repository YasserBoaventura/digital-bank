package com.Digital_Bank.card.service.impl;

import com.Digital_Bank.account.domain.entity.Account;
import com.Digital_Bank.account.domain.enums.AccountStatus;
import com.Digital_Bank.account.repository.AccountRepository;
import com.Digital_Bank.card.domain.entity.Card;
import com.Digital_Bank.card.domain.enums.CardStatus;
import com.Digital_Bank.card.domain.enums.CardType;
import com.Digital_Bank.card.dto.request.CreateCardRequest;
import com.Digital_Bank.card.dto.response.CardResponse;
import com.Digital_Bank.card.repository.CardRepository;
import com.Digital_Bank.card.service.CardService;
import com.Digital_Bank.card.shared.exception.CardNotFoundException;
import com.Digital_Bank.card.shared.exception.CardProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardServiceImpl implements CardService {

        private final CardRepository cardRepository;
        private final AccountRepository accountRepository;

        private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    @Transactional
    public CardResponse create(CreateCardRequest request) {

        Account account = accountRepository
                .findByAccountNumber(request.accountNumber())
                .orElseThrow(() -> new CardProcessingException(
                        "Account not found: " + request.accountNumber()
                ));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new CardProcessingException(
                    "Cannot issue a card for an inactive account"
            );
        }

            String lastFour = String.format(
                    "%04d",
                    RANDOM.nextInt(10_000)
            );

        Card card = Card.builder()
                .account(account)
                .maskedNumber("**** **** **** " + lastFour)
                .lastFour(lastFour)
                .type(request.type())
                .brand(request.brand())
                .status(CardStatus.ACTIVE)
                .expiryDate(LocalDate.now().plusYears(4))
                .build();

        Card savedCard = cardRepository.save(card);

            return mapToResponse(savedCard);
        }

        @Override
        public CardResponse findById(UUID id) {
            return mapToResponse(getCard(id));
        }

        @Override
        public CardResponse findByReference(String reference) {
            Card card = cardRepository.findByReference(reference)
                    .orElseThrow(() -> new CardNotFoundException(
                            "Card not found with reference: " + reference
                    ));

            return mapToResponse(card);
        }

        @Override
        public Page<CardResponse> findByAccountNumber(
                String accountNumber,
                Pageable pageable
        ) {
            Page<CardResponse> cards = cardRepository
                    .findByAccountAccountNumber(accountNumber, pageable)
                    .map(this::mapToResponse);

            if (cards.isEmpty()) {
                throw new CardNotFoundException(
                        "No cards found for account number: " + accountNumber
                );
            }

            return cards;
        }

    @Override
    public Page<CardResponse> findByAccountNumberAndStatus(
            String accountNumber,
            CardStatus status,
            Pageable pageable
    ) {
        Page<CardResponse> cards = cardRepository
                .findByAccountAccountNumberAndStatus(
                        accountNumber, status, pageable
                )
                .map(this::mapToResponse);

        if (cards.isEmpty()) {
            throw new CardNotFoundException(
                    "No cards found for this account with status: " + status
            );
        }

        return cards;
    }

    @Override
    public Page<CardResponse> findByAccountNumberAndType(
            String accountNumber,
            CardType type,
            Pageable pageable
    ) {
        Page<CardResponse> cards = cardRepository
                .findByAccountAccountNumberAndType(
                        accountNumber, type, pageable
                )
                .map(this::mapToResponse);

        if (cards.isEmpty()) {
            throw new CardNotFoundException(
                    "No cards found for this account with type: " + type
            );
        }

        return cards;
    }

        @Override
        public Page<CardResponse> findByStatus(
                CardStatus status,
                Pageable pageable
        ) {
            Page<CardResponse> cards = cardRepository
                    .findByStatus(status, pageable)
                    .map(this::mapToResponse);

            if (cards.isEmpty()) {
                throw new CardNotFoundException(
                        "No cards found with status: " + status
                );
            }

            return cards;
        }

    @Override
    @Transactional
    public CardResponse block(UUID id) {
        Card card = getCard(id);

        if (card.getStatus() != CardStatus.ACTIVE) {
            throw new CardProcessingException(
                    "Only active cards can be blocked"
            );
        }

        card.setStatus(CardStatus.BLOCKED);

        return mapToResponse(cardRepository.save(card));
    }

    @Override
    @Transactional
    public CardResponse unblock(UUID id) {
        Card card = getCard(id);

        if (card.getStatus() != CardStatus.BLOCKED) {
            throw new CardProcessingException(
                    "Only blocked cards can be unblocked"
            );
        }

            if (!card.getExpiryDate().isAfter(LocalDate.now())) {
                card.setStatus(CardStatus.EXPIRED);
                cardRepository.save(card);

                throw new CardProcessingException(
                        "Expired cards cannot be unblocked"
                );
            }

            card.setStatus(CardStatus.ACTIVE);

            return mapToResponse(cardRepository.save(card));
        }

        @Override
        @Transactional
        public CardResponse cancel(UUID id) {
            Card card = getCard(id);

            if (card.getStatus() == CardStatus.CANCELLED) {
                throw new CardProcessingException(
                        "Card is already cancelled"
                );
            }

            card.setStatus(CardStatus.CANCELLED);

            return mapToResponse(cardRepository.save(card));
        }

        private Card getCard(UUID id) {
            return cardRepository.findById(id)
                    .orElseThrow(() -> new CardNotFoundException(
                            "Card not found with id: " + id
                    ));
        }

        private CardResponse mapToResponse(Card card) {
            return new CardResponse(
                    card.getId(),
                    card.getAccount().getAccountNumber(),
                    card.getReference(),
                    card.getMaskedNumber(),
                    card.getLastFour(),
                    card.getType(),
                    card.getBrand(),
                    card.getStatus(),
                    card.getExpiryDate(),
                    card.getCreatedAt(),
                    card.getUpdatedAt()
            );
        }

}
