package com.example.qr_bank.controller;

import com.example.qr_bank.dto.request.QrCodeRequestDTO;
import com.example.qr_bank.dto.response.QrCodeResponseDTO;
import com.example.qr_bank.service.QrCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/qr-code")
@RequiredArgsConstructor
@Slf4j
public class QrCodeController {

    private final QrCodeService qrCodeService;

    @Operation(summary = "Generate QR Code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping(value = "/generate",produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQrCodeImage(@RequestBody QrCodeRequestDTO qrCodeRequestDTO) {
        try {
            byte[] qrImageBytes = qrCodeService.generateQrCode(qrCodeRequestDTO);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);  // Görsel PNG formatında
            headers.setContentLength(qrImageBytes.length);

            return new ResponseEntity<>(qrImageBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping(value = "/decode",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<QrCodeRequestDTO> decodeQrCode(@RequestParam("file") MultipartFile file) {
        log.info("QrCodeController::decodeQrCode file {}", file.getOriginalFilename());
        try {
            byte[] fileBytes = file.getBytes();
            QrCodeRequestDTO decodedData = qrCodeService.decodeQrCode(fileBytes);
            return ResponseEntity.ok(decodedData);
        } catch (Exception e) {
            log.error("QrCodeController::decodeQrCode Exception: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
}
