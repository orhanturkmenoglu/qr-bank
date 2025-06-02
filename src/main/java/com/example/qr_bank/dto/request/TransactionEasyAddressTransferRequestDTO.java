package com.example.qr_bank.dto.request;

import com.example.qr_bank.enums.EasyAddressType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEasyAddressTransferRequestDTO {

    @Schema(description = "Sender IBAN", example = "TR330006100519786457841326")
    private String senderIban;

    @Schema(description = "Type of easy address", example = "PHONE_TRANSFER")
    private EasyAddressType easyAddressType;

    @Schema(description = "Recipient identifier (TCKN or phone number)", example = "12345678901")
    private String identityNumber;

    @Schema(description = "Transfer amount", example = "500.00")
    private BigDecimal amount;

    @Schema(description = "Recipient phone number", example = "555-1234")
    private String phoneNumber;

    @Schema(description = "Recipient email address", example = "9t0w8@example.com")
    private String email;

    @Schema(description = "Description or note for the transfer", example = "Payment for invoice #1234")
    private String description;
}
