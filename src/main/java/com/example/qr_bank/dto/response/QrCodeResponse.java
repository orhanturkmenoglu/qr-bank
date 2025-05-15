package com.example.qr_bank.dto.response;

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
public class QrCodeResponse implements Serializable {

    private String qrContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expirationDate;
    private boolean isUsed;
}
