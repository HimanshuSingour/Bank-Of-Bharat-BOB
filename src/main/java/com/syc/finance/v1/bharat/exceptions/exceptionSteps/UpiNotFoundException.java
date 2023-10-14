package com.syc.finance.v1.bharat.exceptions.exceptionSteps;

import lombok.AllArgsConstructor;
import lombok.Data;


public class UpiNotFoundException extends RuntimeException{


    public UpiNotFoundException(String message){

        super(message);
    }
}
