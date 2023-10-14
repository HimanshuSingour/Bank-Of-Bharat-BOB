package com.syc.finance.v1.bharat.dto.UPIPay;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayUsingUpiRequest {

    private String UPI_ID;
    private String contactEmail;
    private double payMoney;
    private String bankPassword;
}
