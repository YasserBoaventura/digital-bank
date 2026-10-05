package com.Digital_Bank.costumer.service.impl;

import com.Digital_Bank.costumer.domain.entity.Customer;
import com.Digital_Bank.costumer.domain.enums.CustomerStatus;
import com.Digital_Bank.costumer.dto.request.CreateCustomerRequest;
import com.Digital_Bank.costumer.dto.request.UpdateCustomerRequest;
import com.Digital_Bank.costumer.dto.response.CustomerResponse;

import com.Digital_Bank.costumer.repository.CustomerRepository;
import com.Digital_Bank.costumer.service.CustomerService;
import com.Digital_Bank.costumer.shared.exception.BussinesException;
import com.Digital_Bank.costumer.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public  class  CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

@Override
public CustomerResponse create(CreateCustomerRequest request) {

    if (customerRepository.existsByEmail(request.email())) {
        throw new BussinesException("Email já está cadastrado");
    }

    if (customerRepository.existsByPhone(request.phone())) {
        throw new BussinesException("Telefone já está cadastrado");
    }

    if (customerRepository.existsByDocumentNumber(request.documentNumber())) {
        throw new BussinesException("Documento já está cadastrado");
    }

    Customer customer = Customer.builder()
            .fullName(request.fullName())
            .email(request.email())
            .phone(request.phone())
            .documentNumber(request.documentNumber())
            .dateOfBirth(request.dateOfBirth())
            .build();

    Customer savedCustomer = customerRepository.save(customer);

    return toResponse(savedCustomer);
}

@Override
@Transactional(readOnly = true)
public CustomerResponse findById(UUID id) {

    Customer customer = customerRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Cliente não encontrado"));

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
public CustomerResponse update(
        UUID id,
        UpdateCustomerRequest request) {

    Customer customer = customerRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Cliente não encontrado"));

    if (request.fullName() != null) {
        customer.setFullName(request.fullName());
    }

    if (request.email() != null &&
            !request.email().equals(customer.getEmail())) {

        if (customerRepository.existsByEmail(request.email())) {
            throw new BussinesException("Email já está cadastrado");
        }

        customer.setEmail(request.email());
    }

    if (request.phone() != null &&
            !request.phone().equals(customer.getPhone())) {

        if (customerRepository.existsByPhone(request.phone())) {
            throw new BussinesException("Telefone já está cadastrado");
        }

        customer.setPhone(request.phone());
    }
    if (request.dateOfBirth() != null) {
        customer.setDateOfBirth(request.dateOfBirth());
    }
    return toResponse(customer);
}

@Override
public void delete(UUID id) {

    Customer customer = customerRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Cliente não encontrado"));

    customer.setStatus(
        CustomerStatus.INACTIVE
    );
}

@Override
public CustomerResponse activate(UUID id) {

Customer customer = customerRepository.findById(id)
        .orElseThrow(() ->
                new ResourceNotFoundException("Cliente não encontrado"));

if (customer.getStatus() == CustomerStatus.BLOCKED) {
    throw new BussinesException(
            "Cliente bloqueado não pode ser ativado diretamente"
    );
}

customer.setStatus(CustomerStatus.ACTIVE);

return toResponse(customer);
}

@Override
public CustomerResponse deactivate(UUID id) {

Customer customer = customerRepository.findById(id)
        .orElseThrow(() ->
                new ResourceNotFoundException("Cliente não encontrado"));

if (customer.getStatus() == CustomerStatus.BLOCKED) {
    throw new BussinesException(
            "Cliente bloqueado não pode ser desativado"
    );
}

customer.setStatus(CustomerStatus.INACTIVE);

return toResponse(customer);
}

@Override
public CustomerResponse block(UUID id) {

Customer customer = customerRepository.findById(id)
        .orElseThrow(() ->
                new ResourceNotFoundException("Cliente não encontrado"));

if (customer.getStatus() == CustomerStatus.BLOCKED) {
    throw new BussinesException(
            "Cliente já está bloqueado"
    );
}

customer.setStatus(CustomerStatus.BLOCKED);

return toResponse(customer);
}
@Override
public CustomerResponse unblock(UUID id) {

Customer customer = customerRepository.findById(id)
        .orElseThrow(() ->
                new ResourceNotFoundException("Cliente não encontrado"));

if (customer.getStatus() != CustomerStatus.BLOCKED) {
    throw new BussinesException(
            "Cliente não está bloqueado"
    );
}

customer.setStatus(CustomerStatus.ACTIVE);

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
