package com.Digital_Bank.card.shared.exception;

public class CardNotFoundException extends  RuntimeException{


    public CardNotFoundException(String message){
        super(message);
    }
}
