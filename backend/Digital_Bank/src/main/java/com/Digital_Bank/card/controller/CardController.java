package com.Digital_Bank.card.controller;


import com.Digital_Bank.card.domain.enums.CardStatus;
import com.Digital_Bank.card.domain.enums.CardType;
import com.Digital_Bank.card.dto.request.CreateCardRequest;
import com.Digital_Bank.card.dto.response.CardResponse;
import com.Digital_Bank.card.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {


        private final CardService cardService;

        @PostMapping
        public ResponseEntity<CardResponse> create(
                @Valid @RequestBody CreateCardRequest request
        ) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(cardService.create(request));
        }

        @GetMapping("/{id}")
        public ResponseEntity<CardResponse> findById(
                @PathVariable UUID id
        ) {
            return ResponseEntity.ok(cardService.findById(id));
        }

        @GetMapping("/reference/{reference}")
        public ResponseEntity<CardResponse> findByReference(
                @PathVariable String reference
        ) {
            return ResponseEntity.ok(
                    cardService.findByReference(reference)
            );
        }

        @GetMapping("/account/{accountNumber}")
        public ResponseEntity<Page<CardResponse>> findByAccountNumber(
                @PathVariable String accountNumber,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size
        ) {
            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by(Sort.Direction.DESC, "createdAt")
            );

            return ResponseEntity.ok(
                    cardService.findByAccountNumber(accountNumber, pageable)
            );
        }

        @GetMapping("/account/{accountNumber}/status/{status}")
        public ResponseEntity<Page<CardResponse>> findByAccountAndStatus(
                @PathVariable String accountNumber,
                @PathVariable CardStatus status,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size
        ) {
            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by(Sort.Direction.DESC, "createdAt")
            );

            return ResponseEntity.ok(
                    cardService.findByAccountNumberAndStatus(
                            accountNumber, status, pageable
                    )
            );
        }

        @GetMapping("/account/{accountNumber}/type/{type}")
        public ResponseEntity<Page<CardResponse>> findByAccountAndType(
                @PathVariable String accountNumber,
                @PathVariable CardType type,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size
        ) {
            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by(Sort.Direction.DESC, "createdAt")
            );

            return ResponseEntity.ok(
                    cardService.findByAccountNumberAndType(
                            accountNumber, type, pageable
                    )
            );
        }

        @GetMapping("/status/{status}")
        public ResponseEntity<Page<CardResponse>> findByStatus(
                @PathVariable CardStatus status,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size
        ) {
            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by(Sort.Direction.DESC, "createdAt")
            );

            return ResponseEntity.ok(
                    cardService.findByStatus(status, pageable)
            );
        }

        @PatchMapping("/{id}/block")
        public ResponseEntity<CardResponse> block(
                @PathVariable UUID id
        ) {
            return ResponseEntity.ok(cardService.block(id));
        }

        @PatchMapping("/{id}/unblock")
        public ResponseEntity<CardResponse> unblock(
                @PathVariable UUID id
        ) {
            return ResponseEntity.ok(cardService.unblock(id));
        }

        @PatchMapping("/{id}/cancel")
        public ResponseEntity<CardResponse> cancel(
                @PathVariable UUID id
        ) {
            return ResponseEntity.ok(cardService.cancel(id));
        }

}
