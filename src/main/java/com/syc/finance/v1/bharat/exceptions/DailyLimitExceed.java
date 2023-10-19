package com.syc.finance.v1.bharat.exceptions;

public class DailyLimitExceed extends RuntimeException{

    public DailyLimitExceed(String message){
        super(message);
    }
}
