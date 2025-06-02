package com.example.qr_bank.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Types of easy addresses supported by the system")
public enum EasyAddressType {

    @Schema(description = "Address via phone number")
    PHONE_TRANSFER,

    @Schema(description = "Address via email")
    EMAIL_TRANSFER,

    @Schema(description = "Address via TCKN")
    TCKN_TRANSFER
}
