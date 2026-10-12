package com.Digital_Bank.payment.controller;
import com.Digital_Bank.payment.domain.enums.PaymentStatus;
import com.Digital_Bank.payment.domain.enums.PaymentType;
import com.Digital_Bank.payment.dto.reponse.PaymentResponse;
import com.Digital_Bank.payment.dto.request.CreatePaymentRequest;

import com.Digital_Bank.payment.service.PaymentService;
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
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

        private final PaymentService paymentService;

        @PostMapping
        public ResponseEntity<PaymentResponse> create(
                @Valid @RequestBody CreatePaymentRequest request) {

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(
                            paymentService.create(request)
                    );
        }

        @GetMapping("/{id}")
        public ResponseEntity<PaymentResponse> findById(
                @PathVariable UUID id
        ) {
            return ResponseEntity.ok(
                    paymentService.findById(id)
            );
        }

        @GetMapping("/reference/{reference}")
        public ResponseEntity<PaymentResponse> findByReference(
                @PathVariable String reference
        ) {
            return ResponseEntity.ok(
                    paymentService.findByReference(
                            reference
                    )
            );
        }

        @GetMapping("/account/{accountNumber}")
        public ResponseEntity<Page<PaymentResponse>>
        findByAccountNumber(

                @PathVariable String accountNumber,

                @RequestParam(
                        defaultValue = "0"
                )
                int page,

                @RequestParam(
                        defaultValue = "10"
                )
                int size
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
                    paymentService.findByAccountNumber(
                            accountNumber,
                            pageable
                    )
            );
        }

        @GetMapping(
                "/account/{accountNumber}/status/{status}"
        )
        public ResponseEntity<Page<PaymentResponse>>
        findByAccountNumberAndStatus(@PathVariable String accountNumber, @PathVariable PaymentStatus status,
                @RequestParam(
                        defaultValue = "0"
                )
                int page,

                @RequestParam(
                        defaultValue = "10"
                )
                int size
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
                    paymentService
                            .findByAccountNumberAndStatus(
                                    accountNumber,
                                    status,
                                    pageable
                            )
            );
        }

        @GetMapping("/account/{accountNumber}/type/{type}")
        public ResponseEntity<Page<PaymentResponse>>
        findByAccountNumberAndType(@PathVariable String accountNumber, @PathVariable PaymentType type,

                @RequestParam(
                        defaultValue = "0"
                )
                int page,

                @RequestParam(
                        defaultValue = "10"
                )
                int size
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
                    paymentService
                            .findByAccountNumberAndType(
                                    accountNumber,
                                    type,
                                    pageable
                            )
            );
        }

        @GetMapping("/status/{status}")
        public ResponseEntity<Page<PaymentResponse>>
        findByStatus(

                @PathVariable PaymentStatus status,

                @RequestParam(
                        defaultValue = "0"
                )
                int page,

                @RequestParam(
                        defaultValue = "10"
                )
                int size
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
                    paymentService.findByStatus(
                            status,
                            pageable
                    )
            );
        }

        @GetMapping("/type/{type}")
        public ResponseEntity<Page<PaymentResponse>>
        findByType(

                @PathVariable PaymentType type,

                @RequestParam(
                        defaultValue = "0"
                )
                int page,

                @RequestParam(
                        defaultValue = "10"
                )
                int size
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
                    paymentService.findByType(
                            type,
                            pageable
                    )
            );
        }

}
