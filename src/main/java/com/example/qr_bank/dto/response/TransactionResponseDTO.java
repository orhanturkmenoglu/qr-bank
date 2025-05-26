package com.example.qr_bank.dto.response;

import com.example.qr_bank.enums.TransactionStatus;
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
public class TransactionResponseDTO implements Serializable {

    private String id;
    private String senderAccountIban;
    private String receiverAccountIban;
    private BigDecimal amount;
    private TransactionStatus status;
    private LocalDateTime timeStamp;
    private String description;

}
