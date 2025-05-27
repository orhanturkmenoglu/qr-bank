package com.example.qr_bank.dto.request;

import com.example.qr_bank.enums.CurrencyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request data for creating an account")
public class AccountRequestDTO implements Serializable {

    @NotNull(message = "Currency type must be provided")
    @Schema(description = "Currency type for the account", example = "TRY")
    private CurrencyType currency;
}
