package com.syc.finance.v1.bharat.dto.Credit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditResponse {

    private String creditId;
    private double currentBalance;
    private String accountHolderName;
    private String accountNumber;
    private String ifscCode;
    private String bankName;
    private String statusMoney;
    private LocalDateTime localDateTime;


}
