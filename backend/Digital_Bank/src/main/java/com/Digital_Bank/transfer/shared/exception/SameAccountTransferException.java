package com.Digital_Bank.transfer.shared.exception;

public class SameAccountTransferException extends  RuntimeException{

public SameAccountTransferException(String message){
    super(message);
}
}
