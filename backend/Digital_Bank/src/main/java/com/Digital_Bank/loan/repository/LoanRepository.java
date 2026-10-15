package com.Digital_Bank.loan.repository;



import com.Digital_Bank.loan.domain.entity.Loan;
import com.Digital_Bank.loan.domain.enums.LoanStatus;
import com.Digital_Bank.loan.domain.enums.LoanType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoanRepository extends JpaRepository<Loan, UUID> {

        Optional<Loan> findByReference(String reference);

        Page<Loan> findByAccountAccountNumber(
                String accountNumber,
                Pageable pageable
        );

        Page<Loan> findByAccountAccountNumberAndStatus(
                String accountNumber,
                LoanStatus status,
                Pageable pageable
        );

        Page<Loan> findByStatus(
                LoanStatus status,
                Pageable pageable
        );

        Page<Loan> findByType(
                LoanType type,
                Pageable pageable
        );

        Page<Loan> findByAccountAccountNumberAndType(
                String accountNumber,
                LoanType type,
                Pageable pageable
        );

        boolean existsByReference(String reference);

}
