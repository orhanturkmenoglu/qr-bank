package com.example.qr_bank.controller;

import com.example.qr_bank.dto.request.TransactionOperationRequestDTO;
import com.example.qr_bank.dto.request.TransactionRequestDTO;
import com.example.qr_bank.dto.response.TransactionOperationResponseDTO;
import com.example.qr_bank.dto.response.TransactionResponseDTO;
import com.example.qr_bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/send")
    public ResponseEntity<TransactionResponseDTO> sendMoney(@RequestBody TransactionRequestDTO requestDTO) {
        TransactionResponseDTO response = transactionService.sendMoney(requestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/receiver")
    public ResponseEntity<TransactionResponseDTO> receiveMoney(@RequestParam(value = "transactionId") String transactionId,
                                                               @RequestParam(value = "receiverAccountIban") String receiverAccountIban) {
        TransactionResponseDTO response = transactionService.receiverMoney(transactionId,receiverAccountIban);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionOperationResponseDTO> depositMoney(@RequestBody TransactionOperationRequestDTO transactionOperationRequestDTO) {
        TransactionOperationResponseDTO response = transactionService.depositMoney(transactionOperationRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionOperationResponseDTO> withdrawMoney(@RequestBody TransactionOperationRequestDTO transactionOperationRequestDTO) {
        TransactionOperationResponseDTO response = transactionService.withdrawMoney(transactionOperationRequestDTO);
        return ResponseEntity.ok(response);
    }
}
