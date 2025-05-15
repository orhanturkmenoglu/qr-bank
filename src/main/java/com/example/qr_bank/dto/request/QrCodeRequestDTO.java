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
public class QrCodeRequestDTO implements Serializable {

    private String senderAccountId;
    private BigDecimal amount;
}
