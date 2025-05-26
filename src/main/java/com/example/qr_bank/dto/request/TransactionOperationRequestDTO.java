package com.example.qr_bank.dto.request;

import com.example.qr_bank.enums.TransactionType;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionOperationRequestDTO implements Serializable {

    private String accountIban;
    private BigDecimal amount;
    private TransactionType transactionType;
    private String description;
}
