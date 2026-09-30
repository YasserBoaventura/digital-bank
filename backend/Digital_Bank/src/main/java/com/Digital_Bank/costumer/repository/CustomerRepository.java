package com.Digital_Bank.costumer.repository;


import com.Digital_Bank.costumer.domain.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {


    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByDocumentNumber(String documentNumber);

    Optional<Customer> findByEmail(String email);
    Page<Customer> findByFullNameContainingIgnoreCase(
            String fullName,
            Pageable pageable
    );

    Customer save(Customer customer);
}
