package com.example.qr_bank.utils;

public class GenerateIban {

    public static String generateIban() {
        String countryCode = "TR";
        String checkDigits = "00";

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 22; i++) {
            int digit = (int) (Math.random() * 10);
            builder.append(digit);
        }

        String rawDigits = builder.toString();

        String formatted = rawDigits.substring(0,4)+" "+
                rawDigits.substring(4,8)+" "+
                rawDigits.substring(8,12)+" "+
                rawDigits.substring(12,16)+" "+
                rawDigits.substring(16,20)+" "+
                rawDigits.substring(20,22);

        return countryCode + checkDigits +" "+formatted;
    }

}
