package com.syc.finance.v1.bharat.dto.Accounts.Transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

    private String accountId;
    private String accountNumber;
    private String ifscCode;
    private String debitedOrCredited;
    private LocalDateTime localDateTime;
    private double debitOrCreditMoney;
}
