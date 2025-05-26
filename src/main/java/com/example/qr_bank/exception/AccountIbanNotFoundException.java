package com.example.qr_bank.exception;

public class AccountIbanNotFoundException extends RuntimeException {
    public AccountIbanNotFoundException(String message) {
        super(message);
    }
}
