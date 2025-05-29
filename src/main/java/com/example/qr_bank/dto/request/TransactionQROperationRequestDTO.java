package com.example.qr_bank.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request data for a QR code payment operation")
public class TransactionQROperationRequestDTO {

    @Schema(description = "The IBAN of the account involved in the transaction", example = "TR000000000000000000000000", required = true)
    private String qrCode;
}
