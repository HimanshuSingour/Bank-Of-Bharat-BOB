package com.syc.finance.v1.bharat.dto.Update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAmountManually {

    private String accountNumber;
    private double accountBalance;
}
