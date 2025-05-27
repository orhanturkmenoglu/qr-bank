package com.example.qr_bank.dto.response;

import com.example.qr_bank.enums.TransactionStatus;
import com.example.qr_bank.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response DTO for transaction operation details")
public class TransactionOperationResponseDTO {

    @Schema(description = "IBAN of the account involved in the transaction", example = "TR330006100519786457841326")
    private String accountIban;

    @Schema(description = "Amount involved in the transaction", example = "1000.50")
    private BigDecimal amount;

    @Schema(description = "Status of the transaction", example = "COMPLETED")
    private TransactionStatus status;

    @Schema(description = "Timestamp when the transaction occurred", example = "2025-05-27T14:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Type of transaction", example = "DEPOSIT")
    private TransactionType transactionType;

    @Schema(description = "Description or note for the transaction", example = "Salary deposit for May")
    private String description;
}
