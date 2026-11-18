
package com.Digital_Bank.costumer.controller;

import java.util.List;
import java.util.UUID;

import com.Digital_Bank.costumer.dto.request.CreateCustomerRequest;
import com.Digital_Bank.costumer.dto.request.UpdateCustomerRequest;
import com.Digital_Bank.costumer.dto.response.CustomerResponse;
import com.Digital_Bank.costumer.service.CustomerService;
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

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH +"/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> create(
            @Valid @RequestBody CreateCustomerRequest request
    ) {
        CustomerResponse response = customerService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Customer created successfully",
                        response
                ));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<CustomerResponse>>> search(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("fullName").ascending()
        );

        Page<CustomerResponse> response =
                customerService.search(name, pageable);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customers retrieved successfully",
                        response
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> findById(
            @PathVariable UUID id
    ) {
        CustomerResponse response = customerService.findById(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer retrieved successfully",
                        response
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> findAll() {

        List<CustomerResponse> response = customerService.findAll();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customers retrieved successfully",
                        response
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCustomerRequest request
    ) {
        CustomerResponse response = customerService.update(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer updated successfully",
                        response
                )
        );
    }

    // ACTIVATE CUSTOMER
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<CustomerResponse>> activate(
            @PathVariable UUID id
    ) {
        CustomerResponse response = customerService.activate(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer activated successfully",
                        response
                )
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<CustomerResponse>> deactivate(
            @PathVariable UUID id
    ) {
        CustomerResponse response = customerService.deactivate(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer deactivated successfully",
                        response
                )
        );
    }


    @PatchMapping("/{id}/block")
    public ResponseEntity<ApiResponse<CustomerResponse>> block(
            @PathVariable UUID id
    ) {
        CustomerResponse response = customerService.block(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer blocked successfully",
                        response
                )
        );
    }

    @PatchMapping("/{id}/unblock")
    public ResponseEntity<ApiResponse<CustomerResponse>> unblock(
            @PathVariable UUID id
    ) {
        CustomerResponse response = customerService.unblock(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer unblocked successfully",
                        response
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id
    ) {
        customerService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer deleted successfully",
                        null
                )
        );
    }
}
