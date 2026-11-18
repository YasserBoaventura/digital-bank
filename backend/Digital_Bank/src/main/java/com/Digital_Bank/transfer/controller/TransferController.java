package com.Digital_Bank.transfer.controller;
import com.Digital_Bank.costumer.dto.response.CustomerResponse;
import com.Digital_Bank.shared.constants.ApiConstants;
import com.Digital_Bank.shared.response.ApiResponse;
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
@RequestMapping(ApiConstants.API_BASE_PATH +"/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

        @PostMapping
        public ResponseEntity<ApiResponse<TransferResponse>> create(
                @Valid @RequestBody CreateTransferRequest request) {
             TransferResponse response = transferService.create(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(
                            "Transfer created successfully",
                            response
                    ));
        }
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<TransferResponse>> findById(
                @PathVariable UUID id) {
   TransferResponse response = transferService.findById(id);
            return ResponseEntity.ok(
                    ApiResponse.success("Transfer filtered by id retrieved successfully",
                  response
            ));
        }
        @GetMapping("/reference/{reference}")
        public ResponseEntity<ApiResponse<TransferResponse>> findByReference(
                @PathVariable String reference) {

            TransferResponse response = transferService.findByReference(reference);

            return ResponseEntity.ok(
                    ApiResponse.success("Transfer filtered by reference retrieved successfully",
                            response
                    ));

        }

        @GetMapping("/sender/{accountNumber}")
        public ResponseEntity<ApiResponse<Page<TransferResponse>>> findBySenderAccount(
                @PathVariable  String accountNumber,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size
        ) {
            Pageable pageable = createPageable(page, size);
            Page<TransferResponse> response =   transferService.findBySenderAccount(
                    accountNumber,
                    pageable
            );

            return ResponseEntity.ok(
                    ApiResponse.success("Transfer filtered by reference retrieved successfully",
                            response
                    ));


        }
        @GetMapping("/receiver/{accountNumber}")
        public ResponseEntity<Page<TransferResponse>> findByReceiverAccount(
                @PathVariable String accountNumber,
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
                            accountNumber,
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
    private Pageable createPageable(int page, int size) {
        return PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
    }


}
