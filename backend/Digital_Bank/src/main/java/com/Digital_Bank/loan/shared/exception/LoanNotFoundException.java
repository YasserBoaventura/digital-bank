package com.Digital_Bank.loan.shared.exception;

public class LoanNotFoundException extends  RuntimeException{

    public LoanNotFoundException(String message){
        super(message);
    }
}
