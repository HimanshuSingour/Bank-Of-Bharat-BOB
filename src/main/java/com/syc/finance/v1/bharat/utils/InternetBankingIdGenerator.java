package com.syc.finance.v1.bharat.utils;

import java.util.Random;

public class InternetBankingIdGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int ID_LENGTH = 15; // Adjust the length as needed

    public String generateInternetBankingId() {
        StringBuilder idBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < ID_LENGTH; i++) {
            char randomChar = CHARACTERS.charAt(random.nextInt(CHARACTERS.length()));
            idBuilder.append(randomChar);
        }

        return idBuilder.toString();
    }
}
