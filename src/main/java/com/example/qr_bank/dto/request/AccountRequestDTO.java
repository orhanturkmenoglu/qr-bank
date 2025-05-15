package com.example.qr_bank.dto.request;

import com.example.qr_bank.enums.CurrencyType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequestDTO implements Serializable {

    @JsonProperty("currency_type")
    private CurrencyType currencyType;
}
