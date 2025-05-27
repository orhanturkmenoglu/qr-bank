package com.example.qr_bank.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response DTO for QR Code details")
public class QrCodeResponseDTO implements Serializable {

    @Schema(description = "The content or data encoded in the QR code", example = "QR1234567890")
    private String qrContent;

    @Schema(description = "The date and time when the QR code was created", example = "2025-05-27T14:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "The date and time when the QR code was last updated", example = "2025-05-27T15:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "The expiration date and time of the QR code", example = "2025-06-27T14:30:00")
    private LocalDateTime expirationDate;

    @Schema(description = "Indicates whether the QR code has been used", example = "false")
    private boolean isUsed;
}
