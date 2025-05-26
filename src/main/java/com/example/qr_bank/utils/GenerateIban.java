package com.example.qr_bank.utils;

import java.util.UUID;

public class GenerateIban {

    public static String generateIban() {
        return "TR" + UUID.randomUUID().toString().replace("-", "").substring(0, 24);
    }

}
