package com.Digital_Bank.costumer.shared.exception;
import java.time.LocalDateTime;
import java.util.*;

import com.Digital_Bank.costumer.shared.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
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

}
