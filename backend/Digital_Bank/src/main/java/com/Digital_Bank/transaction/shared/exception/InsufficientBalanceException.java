package com.Digital_Bank.transaction.shared.exception;

public class InsufficientBalanceException extends  RuntimeException{

    public InsufficientBalanceException(String message){
        super(message);
    }
}
