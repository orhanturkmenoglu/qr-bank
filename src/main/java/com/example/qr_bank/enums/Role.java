package com.example.qr_bank.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User role enum")
public enum Role {
    @Schema(description = "Regular user with limited access")
    ROLE_USER,

    @Schema(description = "Administrator with full access")
    ROLE_ADMIN
}
