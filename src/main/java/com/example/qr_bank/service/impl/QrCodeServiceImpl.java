package com.example.qr_bank.service.impl;

import com.example.qr_bank.dto.request.QrCodeRequestDTO;
import com.example.qr_bank.dto.response.AccountResponseDTO;
import com.example.qr_bank.mapper.AccountMapper;
import com.example.qr_bank.model.Account;
import com.example.qr_bank.model.QrCode;
import com.example.qr_bank.repository.QrCodeRepository;
import com.example.qr_bank.service.AccountService;
import com.example.qr_bank.service.QrCodeService;
import com.example.qr_bank.utils.AESEncryptionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class QrCodeServiceImpl implements QrCodeService {

    private final QrCodeRepository qrCodeRepository;
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    private final ObjectMapper objectMapper;

    private final AESEncryptionUtil AESEncryptionUtil;

    @Override
    public byte[] generateQrCode(QrCodeRequestDTO qrCodeRequestDTO) throws JsonProcessingException {
        log.info("QrCodeServiceImpl::generateQrCode {}", qrCodeRequestDTO);

        try {
            if (ObjectUtils.isEmpty(qrCodeRequestDTO)) {
                log.error("QrCodeServiceImpl::generateQrCode QrCodeRequestDTO is empty");
                throw new IllegalArgumentException("QrCodeRequestDTO is empty");
            }

            // Json Formatına dönüştür.
            String jsonData = objectMapper.writeValueAsString(qrCodeRequestDTO);
            log.info("QrCodeServiceImpl::generateQrCode jsonData {}", jsonData);

            String encryptedData = AESEncryptionUtil.encrypt(jsonData);
            log.info("QrCodeServiceImpl::generateQrCode encryptedData {}", encryptedData);

            // QR CODE URET

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            log.info("QrCodeServiceImpl::generateQrCode hints {}", hints);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(encryptedData, BarcodeFormat.QR_CODE, 250, 250, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", stream);

            log.info("QrCodeServiceImpl::generateQrCode qrCodeWriter {}", qrCodeWriter);
            log.info("QrCodeServiceImpl::generateQrCode bitMatrix {}", bitMatrix);
            log.info("QrCodeServiceImpl::generateQrCode stream {}", stream);


            AccountResponseDTO accountResponseDTO = accountService.getAccountByIban(qrCodeRequestDTO.getAccountIban());
            Account account = accountMapper.toAccountResponseDTO(accountResponseDTO);


            QrCode qrCode = QrCode.builder()
                    .expirationDate(LocalDateTime.now().plusHours(1))
                    .transactionType(qrCodeRequestDTO.getTransactionType())
                    .amount(qrCodeRequestDTO.getAmount())
                    .account(account)
                    .isUsed(false)
                    .build();

            qrCodeRepository.save(qrCode);

            log.info("QrCodeServiceImpl::generateQrCode qrCode {}", qrCode);

            return stream.toByteArray();

        } catch (Exception e) {
            log.error("QrCodeServiceImpl::generateQrCode Exception: {}", e.getMessage());
            throw new RuntimeException("QrCodeServiceImpl::generateQrCode Exception: " + e.getMessage());
        }

    }

    @Override
    public QrCodeRequestDTO decodeQrCode(byte[] imageBytes) {
        log.info("QrCodeServiceImpl::decodeQrCode {}", imageBytes);

        try {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

            if (bufferedImage == null) {
                throw new IllegalArgumentException("Invalid image data");
            }

            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap);

            String decrypted = AESEncryptionUtil.decrypt(result.getText());
            log.info("QrCodeServiceImpl::decodeQrCode decrypted {}", decrypted);

            return objectMapper.readValue(decrypted, QrCodeRequestDTO.class);

        } catch (Exception e) {
            log.error("Failed to decode QR code: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to decode QR code: " + e.getMessage(), e);
        }
    }
}
