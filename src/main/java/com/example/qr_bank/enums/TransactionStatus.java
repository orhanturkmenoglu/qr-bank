package com.example.qr_bank.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status of a transaction")
public enum TransactionStatus {

    @Schema(description = "Transaction is pending and not completed yet")
    PENDING,

    @Schema(description = "Transaction has been successfully completed")
    COMPLETED,

    @Schema(description = "Transaction failed due to an error")
    FAILED
}
