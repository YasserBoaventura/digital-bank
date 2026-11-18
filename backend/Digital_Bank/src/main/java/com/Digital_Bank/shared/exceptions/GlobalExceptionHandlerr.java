package com.Digital_Bank.shared.exceptions;

import com.Digital_Bank.card.shared.exception.CardNotFoundException;
import com.Digital_Bank.card.shared.exception.CardProcessingException;
import com.Digital_Bank.loan.shared.exception.LoanNotFoundException;
import com.Digital_Bank.loan.shared.exception.LoanProcessingException;
import com.Digital_Bank.notification.shared.exception.NotificationNotFoundException;
import com.Digital_Bank.notification.shared.exception.NotificationProcessingException;
import com.Digital_Bank.payment.shared.exception.PaymentNotFoundException;
import com.Digital_Bank.savings.shared.exception.SavingsNotFoundException;
import com.Digital_Bank.savings.shared.exception.SavingsProcessingException;
import com.Digital_Bank.shared.response.ApiResponse;
import com.Digital_Bank.transaction.shared.exception.InsufficientBalanceException;
import com.Digital_Bank.transfer.shared.exception.SameAccountTransferException;
import com.Digital_Bank.transfer.shared.exception.TransferAccountException;
import com.Digital_Bank.transfer.shared.exception.TransferNotFountException;

import jakarta.validation.ConstraintViolationException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandlerr {


    // RESOURCE NOT FOUND
    @ExceptionHandler({
            ResourceNotFoundException.class,
            PaymentNotFoundException.class,
            CardNotFoundException.class,
            NotificationNotFoundException.class,
            LoanNotFoundException.class,
            SavingsNotFoundException.class,
            TransferNotFountException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleNotFound(
            RuntimeException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(exception.getMessage()));
    }

    // BUSINESS EXCEPTION

    @ExceptionHandler(BussinesException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(
            BussinesException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(exception.getMessage()));
    }

    // INSUFFICIENT BALANCE
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiResponse<Void>> handleInsufficientBalance(
            InsufficientBalanceException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error(exception.getMessage()));
    }

    // TRANSFER EXCEPTIONS

    @ExceptionHandler({
            SameAccountTransferException.class,
            TransferAccountException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleTransferException(
            RuntimeException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error(exception.getMessage()));
    }

    // CARD PROCESSING

    @ExceptionHandler(CardProcessingException.class)
    public ResponseEntity<ApiResponse<Void>> handleCardProcessing(
            CardProcessingException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error(exception.getMessage()));
    }

    // NOTIFICATION PROCESSING
    @ExceptionHandler(NotificationProcessingException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotificationProcessing(
            NotificationProcessingException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error(exception.getMessage()));
    }

    // LOAN PROCESSING

    @ExceptionHandler(LoanProcessingException.class)
    public ResponseEntity<ApiResponse<Void>> handleLoanProcessing(
            LoanProcessingException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error(exception.getMessage()));
    }

    // SAVINGS PROCESSING

    @ExceptionHandler(SavingsProcessingException.class)
    public ResponseEntity<ApiResponse<Void>> handleSavingsProcessing(
            SavingsProcessingException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error(exception.getMessage()));
    }

    // VALIDATION ERRORS

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(
            MethodArgumentNotValidException exception
    ) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField() + ": " + error.getDefaultMessage()
                )
                .collect(Collectors.joining("; "));

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(message));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(
            ConstraintViolationException exception
    ) {

        String message = exception.getConstraintViolations()
                .stream()
                .map(violation ->
                        violation.getPropertyPath() + ": "
                                + violation.getMessage()
                )
                .collect(Collectors.joining("; "));

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(message));
    }

    // INVALID JSON

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidRequestBody(
            HttpMessageNotReadableException exception
    ) {

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(
                        "Invalid or malformed request body"
                ));
    }

    // DATABASE CONSTRAINT VIOLATIONS

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrity(
            DataIntegrityViolationException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(
                        "The operation violates a database constraint"
                ));
    }

    // UNEXPECTED ERRORS

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(
            Exception exception
    ) {

        log.error("Unexpected application error", exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        "An unexpected internal error occurred"
                ));
    }
}