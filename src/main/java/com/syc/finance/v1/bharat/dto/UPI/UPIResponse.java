package com.syc.finance.v1.bharat.dto.UPI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UPIResponse {

    private String accountNumber;  // The bank account number linked to the UPI ID
    private String contactNumber;  // The phone number associated with the UPI account
    private String contactEmail;
    private String upiId;  // The UPI ID of the user
    private String UPI_CODE;// The email address associated with the UPI account
    private double UPI_BALANCE;  // The current balance in the UPI account
    private String responseMessage;

}
