package com.syc.finance.v1.bharat.exceptions;

public class EmailAlreadyExistStep extends RuntimeException{

    public EmailAlreadyExistStep(String message){
        super(message);
    }
}
