package com.syc.finance.v1.bharat.exceptions;

import com.syc.finance.v1.bharat.Utils.AccountDetailsForExceptionalHandler;
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
