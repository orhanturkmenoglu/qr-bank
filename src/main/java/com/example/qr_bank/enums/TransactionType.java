package com.example.qr_bank.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Types of transactions supported by the system")
public enum TransactionType {

    @Schema(description = "Transaction via QR code send")
    QR_SEND,

    @Schema(description = "Transaction via QR code receive")
    QR_RECEIVE,

    @Schema(description = "Transaction via QR code deposit")
    QR_DEPOSIT,

    @Schema(description = "Transaction via QR code withdrawal")
    QR_WITHDRAWAL,

    @Schema(description = "Deposit transaction")
    DEPOSIT,

    @Schema(description = "Withdrawal transaction")
    WITHDRAWAL,

    @Schema(description = "Transfer transaction")
    TRANSFER,

    @Schema(description = "Electronic Funds Transfer")
    EFT,

    @Schema(description = "Internal transfer between accounts")
    HAVALE
}
