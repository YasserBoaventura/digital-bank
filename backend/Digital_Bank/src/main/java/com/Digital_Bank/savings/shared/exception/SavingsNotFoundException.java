package com.Digital_Bank.savings.shared.exception;

public class SavingsNotFoundException extends  RuntimeException{


    public SavingsNotFoundException(String message){
        super(message);
    }
}
