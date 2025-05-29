package com.example.qr_bank.dto.request;

import com.example.qr_bank.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QrCodeRequestDTO implements Serializable {

    @NotEmpty(message = "IBAN is required")
    @Schema(description = "IBAN of the account involved in the transaction", example = "TR330006100519786457841326")
    private String accountIban;

    @NotEmpty(message = "Transaction type is required")
    @Schema(description = "Type of transaction", example = "QR_DEPOSIT")
    private TransactionType transactionType; // "QR_DEPOSIT" veya "QR_WITHDRAWAL"

    @NotEmpty(message = "Description is required")
    @Schema(description = "Description or note for the transaction", example = "Salary deposit for May")
    private String description;


    @NotEmpty(message = "Amount is required")
    @Schema(description = "Amount involved in the transaction", example = "1000.50")
    private BigDecimal amount;

}
