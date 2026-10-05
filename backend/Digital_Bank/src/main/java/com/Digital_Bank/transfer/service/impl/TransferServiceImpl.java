package com.Digital_Bank.transfer.service.impl;
import com.Digital_Bank.account.domain.entity.Account;
import com.Digital_Bank.account.domain.enums.AccountStatus;
import com.Digital_Bank.account.repository.AccountRepository;
import com.Digital_Bank.transaction.domain.emuns.TransactionStatus;
import com.Digital_Bank.transaction.domain.emuns.TransactionType;
import com.Digital_Bank.transaction.domain.entity.Transaction;

import com.Digital_Bank.transaction.repository.TransactionRepository;
import com.Digital_Bank.transaction.service.TransactionService;
import com.Digital_Bank.transfer.domain.entity.Transfer;
import com.Digital_Bank.transfer.domain.enums.TransferStatus;
import com.Digital_Bank.transfer.domain.enums.TransferType;

import com.Digital_Bank.transfer.dto.reponse.CreateTransferRequest;
import com.Digital_Bank.transfer.dto.request.TransferResponse;
import com.Digital_Bank.transfer.repository.TransferRepository;
import com.Digital_Bank.transfer.service.TransferService;
import com.Digital_Bank.transfer.shared.exception.SameAccountTransferException;
import com.Digital_Bank.transfer.shared.exception.TransferAccountException;

import com.Digital_Bank.transaction.shared.exception.InsufficientBalanceException;
import com.Digital_Bank.transfer.shared.exception.TransferNotFountException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TransferServiceImpl  implements TransferService {


        private final TransferRepository transferRepository;
        private final AccountRepository accountRepository;
        private final TransactionRepository transactionRepository;

        @Transactional
        @Override
        public TransferResponse create(CreateTransferRequest request) {

        if (request.senderAccountId()
                .equals(request.receiverAccountId())) {

            throw new SameAccountTransferException(
                    "Sender and receiver accounts must be different"
            );
        }

        Account sender = accountRepository
                .findByIdForUpdate(request.senderAccountId())
                .orElseThrow(() ->
                        new TransferAccountException(
                                "Sender account not found"
                        )
                );

        Account receiver = accountRepository
                .findByIdForUpdate(request.receiverAccountId())
                .orElseThrow(() ->
                        new TransferAccountException(
                                "Receiver account not found"
                        )
                );

        validateAccount(sender, "Sender account");
        validateAccount(receiver, "Receiver account");

        BigDecimal amount = request.amount();

            BigDecimal senderBalanceBefore = sender.getBalance();
            BigDecimal receiverBalanceBefore = receiver.getBalance();

            if (senderBalanceBefore.compareTo(amount) < 0) {

                throw new InsufficientBalanceException(
                        "Insufficient account balance"
                );
            }

            //Debit sender

            BigDecimal senderBalanceAfter =
                    senderBalanceBefore.subtract(amount);

            sender.setBalance(senderBalanceAfter);


             // Credit receiver

            BigDecimal receiverBalanceAfter =
                    receiverBalanceBefore.add(amount);

            receiver.setBalance(receiverBalanceAfter);

            accountRepository.save(sender);
            accountRepository.save(receiver);

            /*
             * Create transfer
             */
            Transfer transfer = Transfer.builder()
                    .senderAccount(sender)
                    .receiverAccount(receiver)
                    .type(TransferType.INTERNAL)
                    .status(TransferStatus.COMPLETED)
                    .amount(amount)
                    .description(request.description())
                    .build();

            Transfer savedTransfer =
                    transferRepository.save(transfer);

            /*
             * Transaction for sender
             */
        Transaction senderTransaction = Transaction.builder()
                .account(sender)
                .type(TransactionType.TRANSFER_OUT)
                .status(TransactionStatus.COMPLETED)
                .amount(amount)
                .balanceBefore(senderBalanceBefore)
                .balanceAfter(senderBalanceAfter)
                .description(
                        request.description() != null
                                ? request.description()
                                : "Transfer to account "
                                + receiver.getAccountNumber()
                )
                .build();

            /*
             * Transaction for receiver
             */
        Transaction receiverTransaction = Transaction.builder()
                .account(receiver)
                .type(TransactionType.TRANSFER_IN)
                .status(TransactionStatus.COMPLETED)
                .amount(amount)
                .balanceBefore(receiverBalanceBefore)
                .balanceAfter(receiverBalanceAfter)
                .description(
                        request.description() != null
                                ? request.description()
                                : "Transfer from account "
                                + sender.getAccountNumber()
                )
                .build();

        transactionRepository.save(senderTransaction);
        transactionRepository.save(receiverTransaction);

        return mapToResponse(savedTransfer);
    }

    @Transactional(readOnly = true)
    @Override
    public TransferResponse findById(UUID id) {

        Transfer transfer = transferRepository
                .findById(id)
                .orElseThrow(() ->
                        new TransferNotFountException(
                                "Transfer not found"
                        )
                );

            return mapToResponse(transfer);
        }

        @Transactional(readOnly = true)
        @Override
        public TransferResponse findByReference(String reference) {

            Transfer transfer = transferRepository
                    .findByReference(reference)
                    .orElseThrow(() ->
                            new TransferNotFountException(
                                    "Transfer not found"
                            )
                    );

            return mapToResponse(transfer);
        }

        @Transactional(readOnly = true)
        @Override
        public Page<TransferResponse> findBySenderAccount(
                UUID accountId,
                Pageable pageable
        ) {

            return transferRepository
                    .findBySenderAccountId(accountId, pageable)
                    .map(this::mapToResponse);
        }

        @Transactional(readOnly = true)
        public Page<TransferResponse> findByReceiverAccount(
                UUID accountId,
                Pageable pageable
        ) {

            return transferRepository
                    .findByReceiverAccountId(accountId, pageable)
                    .map(this::mapToResponse);
        }

        @Transactional(readOnly = true)
        public Page<TransferResponse> findByStatus(
                TransferStatus status,
                Pageable pageable
        ) {

            return transferRepository
                    .findByStatus(status, pageable)
                    .map(this::mapToResponse);
        }

        private void validateAccount(
                Account account,
                String accountDescription) {
            if (account.getStatus() != AccountStatus.ACTIVE) {

                throw new TransferAccountException(
                        accountDescription
                                + " is not active"
                );
            }
        }

        private TransferResponse mapToResponse(
                Transfer transfer
        ) {

            return new TransferResponse(
                    transfer.getId(),
                    transfer.getSenderAccount().getId(),
                    transfer.getReceiverAccount().getId(),
                    transfer.getType(),
                    transfer.getStatus(),
                    transfer.getAmount(),
                    transfer.getReference(),
                    transfer.getDescription(),
                    transfer.getCreatedAt(),
                    transfer.getUpdatedAt()
            );
        }

}
