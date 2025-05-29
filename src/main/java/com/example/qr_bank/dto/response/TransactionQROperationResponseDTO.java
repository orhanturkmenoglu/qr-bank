package com.example.qr_bank.dto.response;

import com.example.qr_bank.enums.TransactionStatus;
import com.example.qr_bank.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "TransactionQROperationResponseDTO data for QR code operation")
public class TransactionQROperationResponseDTO {

    private String transactionId;

    private TransactionType transactionType;

    private TransactionStatus status;

    private LocalDateTime timestamp;
}
