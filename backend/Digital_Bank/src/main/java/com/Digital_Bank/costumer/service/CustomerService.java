package com.Digital_Bank.costumer.service;

import com.Digital_Bank.costumer.dto.request.CreateCustomerRequest;
import com.Digital_Bank.costumer.dto.request.UpdateCustomerRequest;
import com.Digital_Bank.costumer.dto.response.CustomerResponse;
import com.Digital_Bank.costumer.service.impl.CustomerServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.UUID;

public  interface CustomerService  {
    CustomerResponse create(CreateCustomerRequest request);

    CustomerResponse findById(UUID id);

    List<CustomerResponse> findAll();

    CustomerResponse update(UUID id, UpdateCustomerRequest request);

    Page<CustomerResponse> search(String name, Pageable pageable);

    void delete(UUID id);

    CustomerResponse activate(UUID id);

    CustomerResponse deactivate(UUID id);

    CustomerResponse block(UUID id);

    CustomerResponse unblock(UUID id);
}
