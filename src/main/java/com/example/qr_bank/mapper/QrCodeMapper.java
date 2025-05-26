package com.example.qr_bank.mapper;

import com.example.qr_bank.dto.request.QrCodeRequestDTO;
import com.example.qr_bank.dto.response.QrCodeResponseDTO;
import com.example.qr_bank.model.QrCode;

import java.util.List;

public interface QrCodeMapper {

    QrCode toQrCode(QrCodeRequestDTO qrCodeRequestDTO);

    QrCodeResponseDTO toQrCodeResponse(QrCode qrCode);

    List<QrCodeResponseDTO> toQrCodeResponseList(List<QrCode> qrCodes);
}
