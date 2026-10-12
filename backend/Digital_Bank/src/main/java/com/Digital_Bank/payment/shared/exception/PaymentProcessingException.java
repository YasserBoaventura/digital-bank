package com.Digital_Bank.payment.shared.exception;

public class PaymentProcessingException extends  RuntimeException{

    public PaymentProcessingException(String message){
        super(message);
    }
}
