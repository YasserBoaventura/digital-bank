package com.Digital_Bank.costumer.shared.exception;
import java.time.LocalDateTime;
import java.util.*;

import com.Digital_Bank.card.shared.exception.CardNotFoundException;
import com.Digital_Bank.card.shared.exception.CardProcessingException;
import com.Digital_Bank.costumer.shared.response.ApiErrorResponse;
import com.Digital_Bank.loan.shared.exception.LoanNotFoundException;
import com.Digital_Bank.loan.shared.exception.LoanProcessingException;
import com.Digital_Bank.payment.shared.exception.PaymentNotFoundException;
import com.Digital_Bank.savings.shared.exception.SavingsNotFoundException;
import com.Digital_Bank.savings.shared.exception.SavingsProcessingException;
import com.Digital_Bank.transaction.shared.exception.InsufficientBalanceException;
import com.Digital_Bank.transaction.shared.response.ErrorResponse;
import com.Digital_Bank.transfer.shared.exception.SameAccountTransferException;
import com.Digital_Bank.transfer.shared.exception.TransferAccountException;
import com.Digital_Bank.transfer.shared.exception.TransferNotFountException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleNotFound(
            ResourceNotFoundException exception,
            HttpServletRequest request) {

        return new ApiErrorResponse(
                LocalDateTime.now(),
                404,
                "NOT_FOUND",
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(BussinesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleBusiness(
            BussinesException exception) {

        return Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 400,
                "message", exception.getMessage()
        );
    }
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientBalance(
            InsufficientBalanceException ex) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Insufficient balance",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(error);
    }

    @ExceptionHandler(SameAccountTransferException.class)
    public ResponseEntity<ErrorResponse> handleSameAccountTransfer(
            SameAccountTransferException ex) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Invalid transfer",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(error);
    }

    @ExceptionHandler(TransferAccountException.class)
    public ResponseEntity<ErrorResponse> handleTransferAccount(
            TransferAccountException ex) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Invalid account",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(error);
    }
    @ExceptionHandler(TransferNotFountException.class)
    public ResponseEntity<ErrorResponse> handleTransferNotFound(
            TransferNotFountException ex) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Transfer not found",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }


    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentNotFound(
            PaymentNotFoundException ex
    ) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Payment not found",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }


    @ExceptionHandler(CardProcessingException.class)
    public ResponseEntity<ErrorResponse> handleCardProcessing(
            CardProcessingException ex
    ) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Card processing error",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(error);
    }
@ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCardNotFoundException(CardNotFoundException ex){
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Card not found",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }


    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLoanNotFound(
            LoanNotFoundException ex
    ) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Loan not found",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(LoanProcessingException.class)
    public ResponseEntity<ErrorResponse> handleLoanProcessing(
            LoanProcessingException ex
    ) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Loan processing error",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(error);
    }

    @ExceptionHandler(SavingsNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSavingsNotFound(
            SavingsNotFoundException ex) {

        // Ajusta a construção ao teu ErrorResponse existente.



        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Savings not found",
                ex.getMessage(),
                LocalDateTime.now()

        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }


    @ExceptionHandler(SavingsProcessingException.class)
    public ResponseEntity<ErrorResponse> handleSavingsProcessing(
            SavingsProcessingException ex) {


        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Savings processing error",
                ex.getMessage(),
                LocalDateTime.now()

        );
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(error);
    }
}
