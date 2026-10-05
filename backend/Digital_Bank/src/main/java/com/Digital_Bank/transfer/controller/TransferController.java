package com.Digital_Bank.transfer.controller;
import com.Digital_Bank.transfer.domain.enums.TransferStatus;

import com.Digital_Bank.transfer.dto.reponse.CreateTransferRequest;
import com.Digital_Bank.transfer.dto.request.TransferResponse;
import com.Digital_Bank.transfer.service.TransferService;
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
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

        @PostMapping
        public ResponseEntity<TransferResponse> create(
                @Valid @RequestBody CreateTransferRequest request) {

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(transferService.create(request));
        }

        @GetMapping("/{id}")
        public ResponseEntity<TransferResponse> findById(
                @PathVariable UUID id) {

            return ResponseEntity.ok(
                    transferService.findById(id)
            );
        }
        @GetMapping("/reference/{reference}")
        public ResponseEntity<TransferResponse> findByReference(
                @PathVariable String reference) {

            return ResponseEntity.ok(
                    transferService.findByReference(reference)
            );
        }

        @GetMapping("/sender/{accountId}")
        public ResponseEntity<Page<TransferResponse>> findBySenderAccount(
                @PathVariable UUID accountId,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size
        ) {

            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by(
                            Sort.Direction.DESC,
                            "createdAt"
                    )
            );
            return ResponseEntity.ok(
                    transferService.findBySenderAccount(
                            accountId,
                            pageable
                    )
            );
        }

        @GetMapping("/receiver/{accountId}")
        public ResponseEntity<Page<TransferResponse>> findByReceiverAccount(
                @PathVariable UUID accountId,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size
        ) {
            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by(
                            Sort.Direction.DESC,
                            "createdAt"
                    )
            );
            return ResponseEntity.ok(
                    transferService.findByReceiverAccount(
                            accountId,
                            pageable
                    )
            );
        }
        @GetMapping("/status/{status}")
        public ResponseEntity<Page<TransferResponse>> findByStatus(
                @PathVariable TransferStatus status,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size
        ) {

            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by(
                            Sort.Direction.DESC,
                            "createdAt"
                    )
            );

            return ResponseEntity.ok(
                    transferService.findByStatus(
                            status,
                            pageable
                    )
            );
        }

}
