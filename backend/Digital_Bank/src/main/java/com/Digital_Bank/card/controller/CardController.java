package com.Digital_Bank.card.controller;


import com.Digital_Bank.card.domain.enums.CardStatus;
import com.Digital_Bank.card.domain.enums.CardType;
import com.Digital_Bank.card.dto.request.CreateCardRequest;
import com.Digital_Bank.card.dto.response.CardResponse;
import com.Digital_Bank.card.service.CardService;
import com.Digital_Bank.shared.constants.ApiConstants;
import com.Digital_Bank.shared.response.ApiResponse;
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
@RequestMapping(ApiConstants.API_BASE_PATH + "/cards")
@RequiredArgsConstructor
public class CardController {


        private final CardService cardService;

        @PostMapping
        public ResponseEntity<ApiResponse<CardResponse>> create(
                @Valid @RequestBody CreateCardRequest request
        ) {
            CardResponse response = cardService.create(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(
                            "Card created successfully",
                            response
                    ));
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<CardResponse>> findById(
                @PathVariable UUID id
        ) {
            CardResponse response = cardService.findById(id);

            return ResponseEntity.ok(
                    ApiResponse.success(
                            "Card retrieved successfully",
                            response
                    )
            );
        }


        @GetMapping("/reference/{reference}")
        public ResponseEntity<ApiResponse<CardResponse>> findByReference(
                @PathVariable String reference
        ) {
            CardResponse response =
                    cardService.findByReference(reference);

            return ResponseEntity.ok(
                    ApiResponse.success(
                            "Card retrieved successfully",
                            response
                    )
            );
        }

        @GetMapping("/account/{accountNumber}")
        public ResponseEntity<ApiResponse<Page<CardResponse>>> findByAccountNumber(
                @PathVariable String accountNumber,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size
        ) {
            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by(Sort.Direction.DESC, "createdAt")
            );

            Page<CardResponse> response =
                    cardService.findByAccountNumber(accountNumber, pageable);

            return ResponseEntity.ok(
                    ApiResponse.success(
                            "Cards retrieved successfully",
                            response
                    )
            );
        }


        @GetMapping("/account/{accountNumber}/status/{status}")
        public ResponseEntity<ApiResponse<Page<CardResponse>>> findByAccountAndStatus(
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

            Page<CardResponse> response =
                    cardService.findByAccountNumberAndStatus(
                            accountNumber, status, pageable
                    );

            return ResponseEntity.ok(
                    ApiResponse.success(
                            "Cards retrieved successfully",
                            response
                    )
            );
        }

        @GetMapping("/account/{accountNumber}/type/{type}")
        public ResponseEntity<ApiResponse<Page<CardResponse>>> findByAccountAndType(
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

            Page<CardResponse> response =
                    cardService.findByAccountNumberAndType(
                            accountNumber, type, pageable
                    );

            return ResponseEntity.ok(
                    ApiResponse.success(
                            "Cards retrieved successfully",
                            response
                    )
            );
        }

        @GetMapping("/status/{status}")
        public ResponseEntity<ApiResponse<Page<CardResponse>>> findByStatus(
                @PathVariable CardStatus status,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size) {
            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by(Sort.Direction.DESC, "createdAt")
            );

            Page<CardResponse> response =
                    cardService.findByStatus(status, pageable);

            return ResponseEntity.ok(
                    ApiResponse.success(
                            "Cards retrieved successfully",
                            response
                    )
            );
        }


        @PatchMapping("/{id}/block")
        public ResponseEntity<ApiResponse<CardResponse>> block(
                @PathVariable UUID id) {
            CardResponse response = cardService.block(id);

            return ResponseEntity.ok(
                    ApiResponse.success(
                            "Card blocked successfully",
                            response
                    )
            );
        }


        @PatchMapping("/{id}/unblock")
        public ResponseEntity<ApiResponse<CardResponse>> unblock(
                @PathVariable UUID id) {
            CardResponse response = cardService.unblock(id);

            return ResponseEntity.ok(
                    ApiResponse.success(
                            "Card unblocked successfully",
                            response
                    )
            );
        }

        @PatchMapping("/{id}/cancel")
        public ResponseEntity<ApiResponse<CardResponse>> cancel(
                @PathVariable UUID id
        ) {
            CardResponse response = cardService.cancel(id);

            return ResponseEntity.ok(
                    ApiResponse.success(
                            "Card cancelled successfully",
                            response
                    )
            );
        }


}
