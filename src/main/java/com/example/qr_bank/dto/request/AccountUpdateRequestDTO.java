package com.example.qr_bank.dto.request;

import com.example.qr_bank.enums.CurrencyType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountUpdateRequestDTO {

    private CurrencyType currencyType;
    private BigDecimal balance;
}
