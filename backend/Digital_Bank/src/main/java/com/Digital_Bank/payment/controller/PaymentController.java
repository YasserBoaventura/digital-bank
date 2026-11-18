package com.Digital_Bank.payment.controller;
import com.Digital_Bank.payment.domain.enums.PaymentStatus;
import com.Digital_Bank.payment.domain.enums.PaymentType;
import com.Digital_Bank.payment.dto.reponse.PaymentResponse;
import com.Digital_Bank.payment.dto.request.CreatePaymentRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.Digital_Bank.payment.service.PaymentService;
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
@RequestMapping(ApiConstants.API_BASE_PATH+"/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> create(
            @Valid @RequestBody CreatePaymentRequest request) {
        PaymentResponse response = paymentService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Payment created successfully",
                        response
                ));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponse>> findById(
            @PathVariable UUID id
    ) {

        PaymentResponse response = paymentService.findById(id);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Payment retrieved by accountNumber  successfully ",
                        response
                ));

    }

    @GetMapping("/reference/{reference}")
    public ResponseEntity<ApiResponse<PaymentResponse>> findByReference(
            @PathVariable String reference
    ) {
        PaymentResponse response =   paymentService.findByReference(
                reference);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Payment retrieved successfully",
                        response
                ));

    }

        @GetMapping("/account/{accountNumber}")
        public ResponseEntity<ApiResponse<Page<PaymentResponse>>>
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

        Page<PaymentResponse> response =   paymentService.findByAccountNumber(
                accountNumber,
                pageable
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Account payments filtered by accountNumber retrieved successfully",
                        response
                )
        );

        }
@GetMapping("/account/{accountNumber}/status/{status}")
public ResponseEntity<ApiResponse<Page<PaymentResponse>>> findByAccountNumberAndStatus(
        @PathVariable String accountNumber,
        @PathVariable PaymentStatus status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
) {
    Pageable pageable = createPageable(page, size);

    Page<PaymentResponse> response =
            paymentService.findByAccountNumberAndStatus(
                    accountNumber,
                    status,
                    pageable
            );

    return ResponseEntity.ok(
            ApiResponse.success(
                    "Account payments filtered by status retrieved successfully",
                    response
            )
    );
}
    @GetMapping("/account/{accountNumber}/type/{type}")
    public ResponseEntity<ApiResponse<Page<PaymentResponse>>>
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

        Page<PaymentResponse> response =   paymentService
                .findByAccountNumberAndType(
                        accountNumber,
                        type,
                        pageable
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Account payments filtered by type retrieved successfully",
                        response
                )
        );
    }
        @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<Page<PaymentResponse>>> findByStatus(
            @PathVariable PaymentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = createPageable(page, size);

        Page<PaymentResponse> response =
                paymentService.findByStatus(status, pageable);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Payments filtered by status retrieved successfully",
                        response
                )
        );
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<Page<PaymentResponse>>> findByType(
            @PathVariable PaymentType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = createPageable(page, size);

        Page<PaymentResponse> response =
                paymentService.findByType(type, pageable);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Payments filtered by type retrieved successfully",
                        response
                )
        );
    }
    // Reusable pagination method
    private Pageable createPageable(int page, int size) {
        return PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
    }


}
