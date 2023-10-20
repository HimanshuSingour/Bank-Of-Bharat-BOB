package com.syc.finance.v1.bharat.utils;

import com.syc.finance.v1.bharat.entity.AccountInformation;
import com.syc.finance.v1.bharat.exceptions.DailyLimitExceed;
import com.syc.finance.v1.bharat.service.TransactionService.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AccountlLimitReached {

    @Autowired
    private TransactionService transactionService;

    public void validateDailyTransactionLimit(AccountInformation accountInformation) {

        if (accountInformation != null) {
            int maxTransactionCount = 5;
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime twentyFourHoursAgo = currentDateTime.minusSeconds(10);

            long transactionCount = transactionService.countTransactionsByAccountNumberAndTimestamp(
                    accountInformation.getAccountNumber(),
                    twentyFourHoursAgo,
                    currentDateTime);

            if (transactionCount >= maxTransactionCount) {
                throw new DailyLimitExceed("You have reached the maximum transaction limit for the last 24 hours.");
            }
        }

    }

}
