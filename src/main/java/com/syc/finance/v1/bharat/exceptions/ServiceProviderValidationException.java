package com.syc.finance.v1.bharat.exceptions;

public class ServiceProviderValidationException extends RuntimeException{

    public ServiceProviderValidationException(String messages){
        super(messages);
    }
}
