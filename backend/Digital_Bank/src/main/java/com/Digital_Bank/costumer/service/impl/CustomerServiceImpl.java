package com.Digital_Bank.costumer.service.impl;

import com.Digital_Bank.audit.domain.enums.AuditAction;
import com.Digital_Bank.audit.service.AuditService;
import com.Digital_Bank.costumer.domain.entity.Customer;
import com.Digital_Bank.costumer.domain.enums.CustomerStatus;
import com.Digital_Bank.costumer.dto.request.CreateCustomerRequest;
import com.Digital_Bank.costumer.dto.request.UpdateCustomerRequest;
import com.Digital_Bank.costumer.dto.response.CustomerResponse;

import com.Digital_Bank.costumer.repository.CustomerRepository;
import com.Digital_Bank.costumer.service.CustomerService;
import com.Digital_Bank.costumer.shared.exception.BussinesException;
import com.Digital_Bank.costumer.shared.exception.ResourceNotFoundException;
import com.Digital_Bank.notification.domain.enums.NotificationType;
import com.Digital_Bank.notification.service.NotificationService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public  class  CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final NotificationService notificationService;
    private  final AuditService auditService;

@Override
@Transactional
public CustomerResponse create(@Valid CreateCustomerRequest request) {

    if (customerRepository.existsByEmail(request.email())) {
        throw new BussinesException("The email is already registered");
    }

    if (customerRepository.existsByPhone(request.phone())) {
        throw new BussinesException("The phone number is already registered");
    }

    if (customerRepository.existsByDocumentNumber(request.documentNumber())) {
        throw new BussinesException("The documentNumber is already registered");
    }

    Customer customer = Customer.builder()
            .fullName(request.fullName())
            .email(request.email())
            .phone(request.phone())
            .documentNumber(request.documentNumber())
            .dateOfBirth(request.dateOfBirth())
            .build();

    Customer savedCustomer = customerRepository.save(customer);
//save the notify
    notificationService.createForCustomer(
            savedCustomer.getId(),
            NotificationType.CUSTOMER,
            "Welcome to Digital Bank!",
            "Hello " + savedCustomer.getFullName()
                    + ", your customer registration has been completed successfully. "
                    + "Welcome to Digital Bank!"
    );
    //save the audit use
    auditService.record(
            null,
            AuditAction.CREATE,
            "CUSTOMER",
            savedCustomer.getId().toString(),
            "Customer registered successfully"
    );
    return toResponse(savedCustomer);
}

@Override
@Transactional(readOnly = true)
public CustomerResponse findById(UUID id) {

    Customer customer = customerRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException("customer not found"));

    return toResponse(customer);
}

@Override
@Transactional(readOnly = true)
public Page<CustomerResponse> search(String name, Pageable pageable) {

    return customerRepository
            .findByFullNameContainingIgnoreCase(name, pageable)
            .map(this::toResponse);
}

@Override
@Transactional(readOnly = true)
public List<CustomerResponse> findAll() {

    return customerRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
}

@Override
@Transactional
public CustomerResponse update(
        UUID id,
        UpdateCustomerRequest request) {

    Customer customer = customerRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Costumer not found"));

    if (request.fullName() != null) {
        customer.setFullName(request.fullName());
    }

    if (request.email() != null &&
            !request.email().equals(customer.getEmail())) {

        if (customerRepository.existsByEmail(request.email())) {
            throw new BussinesException("The email is already registered");
        }

        customer.setEmail(request.email());
    }

    if (request.phone() != null &&
            !request.phone().equals(customer.getPhone())) {

        if (customerRepository.existsByPhone(request.phone())) {
            throw new BussinesException("The phone is already registered");
        }

        customer.setPhone(request.phone());
    }
    if (request.dateOfBirth() != null) {
        customer.setDateOfBirth(request.dateOfBirth());
    }
    //save the notify
    notificationService.createForCustomer(
            customer.getId(),
            NotificationType.CUSTOMER,
            "Customer Profile Updated",
            "Your personal information has been updated successfully."
    );

    //save the audit use
    auditService.record(
            null,
            AuditAction.UPDATE,
            "CUSTOMER",
            customer.getId().toString(),
            "Customer Updated successfully"
    );

    return toResponse(customer);
}

@Override
@Transactional
public void delete(UUID id) {
    Customer customer = customerRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Customer not found"));

    customer.setStatus(
        CustomerStatus.INACTIVE
    );
    //save the notify
    notificationService.createForCustomer(
            customer.getId(),
            NotificationType.CUSTOMER,
            "Customer Record Deleted",
            "Your customer record has been deleted."
    );
    //save the audit use
    auditService.record(
            null,
            AuditAction.DELETE,
            "CUSTOMER",
            customer.getId().toString(),
            "Customer deleted successfully"
    );
}
    @Override
    @Transactional
    public CustomerResponse activate(UUID id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        if (customer.getStatus() == CustomerStatus.BLOCKED) {
            throw new BussinesException(
                    "A blocked customer cannot be activated directly."
            );
        }

        customer.setStatus(CustomerStatus.ACTIVE);
// save the notify
        notificationService.createForCustomer(
                customer.getId(),
                NotificationType.CUSTOMER,
                "Customer Account Activated",
                "Your customer account has been activated successfully."
        );
        //save the audit use
        auditService.record(
                null,
                AuditAction.ACTIVATE,
                "CUSTOMER",
                customer.getId().toString(),
                "Customer activated successfully"
        );

        return toResponse(customer);
    }

    @Override
    @Transactional
    public CustomerResponse deactivate(UUID id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        if (customer.getStatus() == CustomerStatus.BLOCKED) {
            throw new BussinesException(
                    "A blocked customer cannot be deactivated."
            );
        }

        customer.setStatus(CustomerStatus.INACTIVE);
// save the notify
notificationService.createForCustomer(
                customer.getId(),
                NotificationType.CUSTOMER,
                "Customer Account Deactivated",
                "Your customer account has been deactivated."
        );
//save the audit use
        auditService.record(
                null,
                AuditAction.DEACTIVATE,
                "CUSTOMER",
                customer.getId().toString(),
                "Customer deactivated successfully"
        );

        return toResponse(customer);
    }

    @Override
    @Transactional
    public CustomerResponse block(UUID id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        if (customer.getStatus() == CustomerStatus.BLOCKED) {
            throw new BussinesException(
                    "Customer is already blocked."
            );
        }

        customer.setStatus(CustomerStatus.BLOCKED);

        notificationService.createForCustomer(
                customer.getId(),
                NotificationType.CUSTOMER,
                "Customer Account Blocked",
                "Your customer account has been blocked."
        );

        auditService.record(
                null,
                AuditAction.BLOCK,
                "CUSTOMER",
                customer.getId().toString(),
                "Customer blocked successfully"
        );
        return toResponse(customer);
    }

    @Override
    @Transactional
    public CustomerResponse unblock(UUID id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        if (customer.getStatus() != CustomerStatus.BLOCKED) {
            throw new BussinesException(
                    "Only blocked customers can be unblocked."
            );
        }

        customer.setStatus(CustomerStatus.ACTIVE);
//save the notify
        notificationService.createForCustomer(
                customer.getId(),
                NotificationType.CUSTOMER,
                "Customer Account Unblocked",
                "Your customer account has been unblocked successfully."
        );
// save the audi use
        auditService.record(
                null,
                AuditAction.UNBLOCK,
                "CUSTOMER",
                customer.getId().toString(),
                "Customer unblocked successfully"
        );
        return toResponse(customer);
    }
private CustomerResponse toResponse(Customer customer) {

    return new CustomerResponse(
            customer.getId(),
            customer.getFullName(),
            customer.getEmail(),
            customer.getPhone(),
            customer.getDocumentNumber(),
            customer.getDateOfBirth(),
            customer.getStatus(),
            customer.getCreatedAt(),
            customer.getUpdatedAt()
    );
}


}
