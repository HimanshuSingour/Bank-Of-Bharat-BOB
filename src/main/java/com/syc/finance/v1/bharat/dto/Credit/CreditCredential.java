package com.syc.finance.v1.bharat.dto.Credit;

import lombok.Data;


@Data
public class CreditCredential {

    private String accountNumber;
    private  String password;
    private String bankPinCode;
    private String ifscCode;
    private String bankName;
    private double creditYourMoney;

}
