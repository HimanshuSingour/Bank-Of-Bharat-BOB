package com.syc.finance.v1.bharat.dto.BalanceEnquiry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceEnquireyRequest {

    private String accountNumber;
    private String password;
    private String ifscCode;

}
