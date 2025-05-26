package com.example.qr_bank.dto.request;

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
public class TransactionRequestDTO implements Serializable {

    private String senderAccountIban;
    private String receiverAccountIban;
    private BigDecimal amount;
    private String description;
}
