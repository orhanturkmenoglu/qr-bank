package com.example.qr_bank.dto.response;

import com.example.qr_bank.enums.TransactionStatus;
import com.example.qr_bank.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionOperationResponseDTO {

    private String accountIban;

    private BigDecimal amount;

    private TransactionStatus status;

    private LocalDateTime timestamp;

    private TransactionType transactionType;

    private String description;
}
