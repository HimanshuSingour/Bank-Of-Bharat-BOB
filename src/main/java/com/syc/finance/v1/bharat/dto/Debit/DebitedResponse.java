package com.syc.finance.v1.bharat.dto.Debit;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebitedResponse {

    private String debitedId;
    private String accountNumber;
    private String ifscCode;
    private double currentBalance;
    private double debitYourMoney;
    private String accountHolderName;
    private String statusDebit;
    private LocalDateTime localDateTime;
}
