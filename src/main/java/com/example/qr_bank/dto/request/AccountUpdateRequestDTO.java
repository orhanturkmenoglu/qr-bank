package com.example.qr_bank.dto.request;

import com.example.qr_bank.enums.CurrencyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request data for updating an account")
public class AccountUpdateRequestDTO {

    @Schema(description = "The currency type for the account", example = "TRY")
    private CurrencyType currencyType;

    @DecimalMin(value = "0.0", inclusive = true, message = "Balance must be zero or positive")
    @Schema(description = "The updated balance for the account", example = "1500.00")
    private BigDecimal balance;
}
