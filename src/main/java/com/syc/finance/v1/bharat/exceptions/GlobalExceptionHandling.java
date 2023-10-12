package com.syc.finance.v1.bharat.exceptions;

import com.syc.finance.v1.bharat.exceptions.exceptionSteps.*;
import com.twilio.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.syc.finance.v1.bharat.Utils.AccountDetailsForExceptionalHandler.PENDING;
import static com.syc.finance.v1.bharat.Utils.AccountDetailsForExceptionalHandler.REJECTED;

@RestControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(EmailAlreadyExistStep.class)
    ResponseEntity<ErrorResponses> emailAlreadyExistException(EmailAlreadyExistStep ex){
        ErrorResponses errorResponses = new ErrorResponses();
        errorResponses.setErrorMessage(ex.getMessage());
        errorResponses.setStatus(REJECTED);
        return new ResponseEntity<>(errorResponses , HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(PhoneNumberAlreadyExistStep.class)
    ResponseEntity<ErrorResponses> phoneAlreadyExistException(PhoneNumberAlreadyExistStep ex){
        ErrorResponses errorResponses = new ErrorResponses();
        errorResponses.setErrorMessage(ex.getMessage());
        errorResponses.setStatus(REJECTED);
        return new ResponseEntity<>(errorResponses , HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NetBankingIdAlreadyExist.class)
    ResponseEntity<ErrorResponses> netBankingIdAlreadyExistException(NetBankingIdAlreadyExist ex){
        ErrorResponses errorResponses = new ErrorResponses();
        errorResponses.setErrorMessage(ex.getMessage());
        errorResponses.setStatus(REJECTED);
        return new ResponseEntity<>(errorResponses , HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotHavingNetbanking.class)
    ResponseEntity<ErrorResponses> netBankingAccountNotPresent(NotHavingNetbanking ex){
        ErrorResponses errorResponses = new ErrorResponses();
        errorResponses.setErrorMessage(ex.getMessage());
        errorResponses.setStatus(REJECTED);
        return new ResponseEntity<>(errorResponses , HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UpiAlreadyExist.class)
    ResponseEntity<ErrorResponses> upiAlreadyExist(UpiAlreadyExist ex){
        ErrorResponses errorResponses = new ErrorResponses();
        errorResponses.setErrorMessage(ex.getMessage());
        errorResponses.setStatus(REJECTED);
        return new ResponseEntity<>(errorResponses , HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ApiException.class)
    ResponseEntity<ApiException> apiExceptionHandling(ApiException ex){
        ErrorResponses errorResponses = new ErrorResponses();
        errorResponses.setErrorMessage(ex.getMessage());
        errorResponses.setStatus(REJECTED);
        return new ResponseEntity<ApiException>(HttpStatus.CREATED);
    }

    @ExceptionHandler(AccountBalanceMinimumSteps.class)
    ResponseEntity<ErrorResponses> accountBalanceExist(AccountBalanceMinimumSteps ex){
        ErrorResponses errorResponses = new ErrorResponses();
        errorResponses.setErrorMessage(ex.getMessage());
        errorResponses.setStatus(REJECTED);
        return new ResponseEntity<>(errorResponses , HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccountNotFoundStep.class)
    ResponseEntity<ErrorResponses> accountNotFound(AccountNotFoundStep ex){
        ErrorResponses errorResponses = new ErrorResponses();
        errorResponses.setErrorMessage(ex.getMessage());
        errorResponses.setStatus(REJECTED);
        return new ResponseEntity<>(errorResponses , HttpStatus.FORBIDDEN);
    }

}
