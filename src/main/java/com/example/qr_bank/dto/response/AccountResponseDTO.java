package com.example.qr_bank.dto.response;

import com.example.qr_bank.enums.CurrencyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response data for account details")
public class AccountResponseDTO implements Serializable {

    @Schema(description = "Unique identifier of the account", example = "a1b2c3d4e5")
    private String id;

    @Schema(description = "IBAN of the account", example = "TR330006100519786457841326")
    private String iban;

    @Schema(description = "Account balance", example = "1000.00")
    private BigDecimal balance;

    @Schema(description = "Daily limit of the account", example = "1000.00")
    private BigDecimal dailyLimit;

    @Schema(description = "Currency type of the account", example = "TRY")
    private CurrencyType currency;

    @Schema(description = "Date and time when the account was created", example = "2025-05-27T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the account was last updated", example = "2025-05-28T11:45:00")
    private LocalDateTime updatedAt;

    @Schema(description = "ID of the user who owns the account", example = "u123456")
    private String ownerId;
}
