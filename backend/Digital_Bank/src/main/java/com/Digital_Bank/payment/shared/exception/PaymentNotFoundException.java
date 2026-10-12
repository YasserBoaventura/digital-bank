package com.Digital_Bank.payment.shared.exception;

public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(String message){
        super(message);
    }
}
