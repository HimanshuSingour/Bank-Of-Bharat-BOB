package com.syc.finance.v1.bharat.dto.UPI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UPIRequest {

    private String accountNumber;
    private String ifscCode;
    private String bankPassword;
    private String contactEmail;
    private String contactNumber;


}
