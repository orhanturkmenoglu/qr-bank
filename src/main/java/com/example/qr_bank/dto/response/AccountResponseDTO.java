package com.example.qr_bank.dto.response;

import com.example.qr_bank.enums.CurrencyType;
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
public class AccountResponseDTO implements Serializable {

    private String id;
    private String iban;
    private BigDecimal balance;
    private CurrencyType currency;
}
