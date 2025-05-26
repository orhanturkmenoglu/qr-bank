package com.example.qr_bank.service;

import com.example.qr_bank.dto.request.QrCodeRequestDTO;
import com.example.qr_bank.dto.response.QrCodeResponseDTO;

public interface QrCodeService {

    QrCodeResponseDTO generateQrCode(QrCodeRequestDTO qrCodeRequestDTO);

    QrCodeResponseDTO decodeQrCode(String qrCodeData);

}
