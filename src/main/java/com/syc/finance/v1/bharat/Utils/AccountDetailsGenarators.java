package com.syc.finance.v1.bharat.Utils;

import java.util.Random;
public class AccountDetailsGenarators {

    public String generateAccountNumber(){
        Random random = new Random();
        long accountNumberLong = random.nextLong() % 1000000000000L + 1000000000000L;
        return String.valueOf(accountNumberLong);
    }

    public String gereratedIFSC() {
        Random random = new Random();
        int branchCodeInt = random.nextInt(9000) + 1000;
        return String.valueOf(branchCodeInt);
    }

    public String generateBankCode() {
        Random random = new Random();
        int bankCodeInt = random.nextInt(9000) + 1000;
        return String.valueOf(bankCodeInt);
    }

    // Generate a fictional branch code (4 digits)
    public String generateBranchCode() {
        Random random = new Random();
        int branchCodeInt = random.nextInt(9000) + 1000;
        return String.valueOf(branchCodeInt);
    }




}
