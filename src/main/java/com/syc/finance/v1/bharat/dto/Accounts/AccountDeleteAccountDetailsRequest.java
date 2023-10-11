package com.syc.finance.v1.bharat.dto.Accounts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDeleteAccountDetailsRequest {

    private String accountHolderName;
    private String password;
    private String accountNumber;
    private String contactEmail;
    private String contactPhone;

}
