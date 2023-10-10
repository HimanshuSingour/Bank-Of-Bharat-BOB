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

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponses> genaralException(Exception ex){
        ErrorResponses errorResponses = new ErrorResponses();
        errorResponses.setErrorMessage("SomeThing Went Wrong ! Try After Sometime");
        errorResponses.setStatus(REJECTED);
        return new ResponseEntity<>(errorResponses , HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(EmailAlreadyExistStep.class)
    ResponseEntity<ErrorResponses> emailAlreadyExistException(EmailAlreadyExistStep ex){
        ErrorResponses errorResponses = new ErrorResponses();
        errorResponses.setErrorMessage("Account creation failed as the provided email address or phone number is already in use.");
        errorResponses.setStatus(REJECTED);
        return new ResponseEntity<>(errorResponses , HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ApiException.class)
    ResponseEntity<ApiException> apiExceptionHandling(ApiException ex){
        ErrorResponses errorResponses = new ErrorResponses();
        errorResponses.setErrorMessage("Number is Not Verified");
        errorResponses.setStatus(REJECTED);
        return new ResponseEntity<ApiException>(HttpStatus.CREATED);
    }

    @ExceptionHandler(AccountBalanceMinimumSteps.class)
    ResponseEntity<ErrorResponses> accountBalanceExcide(AccountBalanceMinimumSteps ex){
        ErrorResponses errorResponses = new ErrorResponses();
        errorResponses.setErrorMessage("Insufficient balance: Amount Less then the minimum money required to debit.");
        errorResponses.setStatus(REJECTED);
        return new ResponseEntity<>(errorResponses , HttpStatus.FORBIDDEN);
    }

}
