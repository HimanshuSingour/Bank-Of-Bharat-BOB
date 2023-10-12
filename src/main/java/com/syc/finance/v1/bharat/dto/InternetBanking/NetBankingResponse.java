package com.syc.finance.v1.bharat.dto.InternetBanking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NetBankingResponse {

    private String netId;
    private String accountHolderName;
    private String net_BANKING_ID;
    private LocalDateTime localDateTime;
    private String accountNumber;
    private String ifscCode;
}
