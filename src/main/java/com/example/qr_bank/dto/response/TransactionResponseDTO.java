package com.example.qr_bank.dto.response;

import com.example.qr_bank.enums.TransactionStatus;
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
@Schema(description = "Transaction response details")
public class TransactionResponseDTO implements Serializable {

    @Schema(description = "Unique identifier of the transaction", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Sender's account IBAN", example = "TR330006100519786457841326")
    private String senderAccountIban;

    @Schema(description = "Receiver's account IBAN", example = "TR330006100519786457841327")
    private String receiverAccountIban;

    @Schema(description = "Transaction amount", example = "250.75")
    private BigDecimal amount;

    @Schema(description = "Status of the transaction", example = "COMPLETED")
    private TransactionStatus status;

    @Schema(description = "Timestamp when the transaction took place", example = "2025-05-27T14:30:00")
    private LocalDateTime timeStamp;

    @Schema(description = "Description or note about the transaction", example = "Payment for invoice #1234")
    private String description;

}
