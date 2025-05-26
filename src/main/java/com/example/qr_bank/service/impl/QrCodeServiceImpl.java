package com.example.qr_bank.service.impl;

import com.example.qr_bank.dto.request.QrCodeRequestDTO;
import com.example.qr_bank.dto.response.QrCodeResponseDTO;
import com.example.qr_bank.service.QrCodeService;
import org.springframework.stereotype.Service;

@Service
public class QrCodeServiceImpl  implements QrCodeService {
    @Override
    public QrCodeResponseDTO generateQrCode(QrCodeRequestDTO qrCodeRequestDTO) {
        return null;
    }

    @Override
    public QrCodeResponseDTO decodeQrCode(String qrCodeData) {
        return null;
    }
}
