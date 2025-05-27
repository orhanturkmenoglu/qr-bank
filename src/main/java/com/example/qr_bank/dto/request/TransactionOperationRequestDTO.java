package com.example.qr_bank.dto.request;

import com.example.qr_bank.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request data for a deposit or withdrawal operation")
public class TransactionOperationRequestDTO implements Serializable {

    @NotBlank(message = "Account IBAN must not be blank")
    @Schema(description = "The IBAN of the account involved in the transaction", example = "TR000000000000000000000000", required = true)
    private String accountIban;

    @NotNull(message = "Amount must be provided")
    @Positive(message = "Amount must be greater than zero")
    @Schema(description = "The amount of money for the transaction", example = "500.00", required = true)
    private BigDecimal amount;

    @NotNull(message = "Transaction type must be specified")
    @Schema(description = "The type of transaction (DEPOSIT or WITHDRAWAL)", example = "DEPOSIT", required = true)
    private TransactionType transactionType;

    @Schema(description = "A description of the transaction", example = "Monthly savings deposit")
    private String description;
}
