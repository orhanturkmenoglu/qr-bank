package com.example.qr_bank.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Currency type enum")
public enum CurrencyType {
    @Schema(description = "US Dollar")
    USD,

    @Schema(description = "Euro")
    EUR,

    @Schema(description = "Turkish Lira")
    TRY
}
