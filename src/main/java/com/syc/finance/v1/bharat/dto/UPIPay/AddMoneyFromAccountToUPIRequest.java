package com.syc.finance.v1.bharat.dto.UPIPay;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMoneyFromAccountToUPIRequest {

    private String upiId;
    private String accountNumber;
    private double payMoney;
}
