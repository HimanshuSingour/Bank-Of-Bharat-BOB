package com.syc.finance.v1.bharat.utils;

import java.util.Random;

public class UPIDGenerater {

    public String generateUpiId(String fullName) {

        if(fullName == null) return null;

        String[] names = fullName.split(" ");

        if (names.length == 2) {

            String firstName = names[0];
            String lastName = names[1];

            if (firstName.isEmpty() || lastName.isEmpty()) {
                return null;
            }

            String upiId = lastName + firstName + "@" + "okbob";
            return upiId;
        }

        return null;
    }

    public String generatePin() {
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        int upiCode = random.nextInt(max - min + 1) + min;
        return String.format("%06d", upiCode); // Corrected the formatting
    }
}
