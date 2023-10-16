package com.syc.finance.v1.bharat.exceptions.exceptionSteps;

public class InSufficientBalance extends RuntimeException{

    public InSufficientBalance(String message){
        super(message);
    }
}
