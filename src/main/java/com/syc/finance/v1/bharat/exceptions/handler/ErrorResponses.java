package com.syc.finance.v1.bharat.exceptions.handler;

import com.syc.finance.v1.bharat.utils.AccountDetailsForExceptionalHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponses {

    private String errorMessage;
    private AccountDetailsForExceptionalHandler status;
}
