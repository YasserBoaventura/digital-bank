package com.Digital_Bank.costumer.controller;
import java.util.*;
import com.Digital_Bank.costumer.dto.request.CreateCustomerRequest;
import com.Digital_Bank.costumer.dto.request.UpdateCustomerRequest;
import com.Digital_Bank.costumer.dto.response.CustomerResponse;
import com.Digital_Bank.costumer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse create(
            @Valid @RequestBody CreateCustomerRequest request) {
        return customerService.create(request);
    }

    @GetMapping("/search")
    public Page<CustomerResponse> search(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("fullName").ascending()
        );

        return customerService.search(name, pageable);
    }
    @GetMapping("/{id}")
    public CustomerResponse findById(@PathVariable UUID id) {
        return customerService.findById(id);
    }
    @GetMapping
    public List<CustomerResponse> findAll() {
        return customerService.findAll();
    }

        @PutMapping("/{id}")
        public CustomerResponse update(
                @PathVariable UUID id,
                @Valid @RequestBody UpdateCustomerRequest request) {

            return customerService.update(id, request);
        }
    @PatchMapping("/{id}/activate")
    public CustomerResponse activate(@PathVariable UUID id) {
        return customerService.activate(id);
    }

    @PatchMapping("/{id}/deactivate")
    public CustomerResponse deactivate(@PathVariable UUID id) {
        return customerService.deactivate(id);
    }

    @PatchMapping("/{id}/block")
    public CustomerResponse block(@PathVariable UUID id) {
        return customerService.block(id);
    }

    @PatchMapping("/{id}/unblock")
    public CustomerResponse unblock(@PathVariable UUID id) {
        return customerService.unblock(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        customerService.delete(id);
    }
}
