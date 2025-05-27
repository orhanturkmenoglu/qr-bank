package com.example.qr_bank.dto.request;

import com.example.qr_bank.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
@Schema(description = "Request data for transferring money between accounts")
public class TransactionRequestDTO implements Serializable {

    @NotBlank(message = "Sender account IBAN must not be blank")
    @Schema(description = "The IBAN of the sender's account", example = "TR000000000000000000000001", required = true)
    private String senderAccountIban;

    @NotBlank(message = "Receiver account IBAN must not be blank")
    @Schema(description = "The IBAN of the receiver's account", example = "TR000000000000000000000002", required = true)
    private String receiverAccountIban;

    @NotNull(message = "Amount must be provided")
    @Positive(message = "Amount must be greater than zero")
    @Schema(description = "The amount to transfer", example = "1500.00", required = true)
    private BigDecimal amount;

    @NotNull(message = "Transaction type must be specified")
    @Schema(description = "The type of transaction (e.g., TRANSFER)", example = "TRANSFER", required = true)
    private TransactionType transactionType;

    @Schema(description = "A description for the transaction", example = "Payment for invoice #1234")
    private String description;
}
