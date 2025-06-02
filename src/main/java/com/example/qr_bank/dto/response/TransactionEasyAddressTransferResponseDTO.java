package com.example.qr_bank.dto.response;

import com.example.qr_bank.enums.EasyAddressType;
import com.example.qr_bank.enums.TransactionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEasyAddressTransferResponseDTO {


    @Schema(description = "Unique identifier of the transaction", example = "t1b2c3d4e5")
    private String transactionId;

    @Schema(description = "Sender's account IBAN", example = "TR330006100519786457841326")
    private String senderAccountId;

    @Schema(description = "Receiver's account IBAN", example = "TR330006100519786457841327")
    private String receiverAccountId;

    @Schema(description = "Transfer amount", example = "500.00")
    private BigDecimal amount;

    @Schema(description = "Description or note for the transfer", example = "Payment for invoice #1234")
    private String description;

    @Schema(description = "Type of easy address", example = "PHONE_TRANSFER")
    private EasyAddressType easyAddressType;

    @Schema(description = "Status of the transaction", example = "COMPLETED")
    private TransactionStatus status;

    @Schema(description = "Timestamp when the transaction took place", example = "2025-05-27T14:30:00")
    private LocalDateTime transactionDate;
}
