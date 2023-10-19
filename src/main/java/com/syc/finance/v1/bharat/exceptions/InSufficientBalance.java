package com.syc.finance.v1.bharat.exceptions;

public class InSufficientBalance extends RuntimeException{

    public InSufficientBalance(String message){
        super(message);
    }
}
