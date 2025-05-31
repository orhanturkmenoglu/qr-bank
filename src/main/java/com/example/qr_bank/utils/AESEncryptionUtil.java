package com.example.qr_bank.utils;


import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AESEncryptionUtil {

    private final AES256TextEncryptor textEncryptor;

    public AESEncryptionUtil(@Value("${spring.security.jasypt.secret}") String secretKey) {
        this.textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(secretKey);
    }

    public String encrypt(String data) {
        return textEncryptor.encrypt(data);
    }

    public String decrypt(String data) {
        return textEncryptor.decrypt(data);
    }
}
