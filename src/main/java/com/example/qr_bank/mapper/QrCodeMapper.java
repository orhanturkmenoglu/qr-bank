package com.example.qr_bank.mapper;

import com.example.qr_bank.dto.request.QrCodeRequestDTO;
import com.example.qr_bank.dto.response.QrCodeResponse;
import com.example.qr_bank.model.QrCode;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QrCodeMapper {

    QrCode toQrCode(QrCodeRequestDTO qrCodeRequestDTO);

    QrCodeResponse toQrCodeResponse(QrCode qrCode);

    List<QrCodeResponse> toQrCodeResponseList(List<QrCode> qrCodes);
}
