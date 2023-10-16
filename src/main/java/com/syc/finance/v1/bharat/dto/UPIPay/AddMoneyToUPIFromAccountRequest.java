package com.syc.finance.v1.bharat.dto.UPIPay;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMoneyToUPIFromAccountRequest {

    private double addedFromUpi;
    private String accountNumber;
    private String password;
    private String upiId;
}
