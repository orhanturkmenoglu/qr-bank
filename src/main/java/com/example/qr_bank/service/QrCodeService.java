package com.example.qr_bank.service;

import com.example.qr_bank.dto.request.QrCodeRequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface QrCodeService {

    byte[] generateQrCode(QrCodeRequestDTO qrCodeRequestDTO) throws JsonProcessingException;

    QrCodeRequestDTO decodeQrCode(byte[] imageBytes);

}
