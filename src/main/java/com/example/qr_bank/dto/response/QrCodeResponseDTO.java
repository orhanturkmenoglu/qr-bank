package com.example.qr_bank.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response DTO for QR Code details")
public class QrCodeResponseDTO implements Serializable {

    @Schema(description = "Unique identifier of the QR code", example = "a1b2c3d4e5")
    private String qrCodeBase64; // Base64 formatında image
}
