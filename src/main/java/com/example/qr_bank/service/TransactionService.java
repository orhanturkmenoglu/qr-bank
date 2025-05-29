package com.example.qr_bank.service;


import com.example.qr_bank.dto.request.TransactionOperationRequestDTO;
import com.example.qr_bank.dto.request.TransactionQROperationRequestDTO;
import com.example.qr_bank.dto.request.TransactionRequestDTO;
import com.example.qr_bank.dto.response.TransactionOperationResponseDTO;
import com.example.qr_bank.dto.response.TransactionQROperationResponseDTO;
import com.example.qr_bank.dto.response.TransactionResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface TransactionService {

    TransactionResponseDTO sendMoney(TransactionRequestDTO transactionRequestDTO);

    TransactionResponseDTO receiverMoney(String transactionId,
                                         String receiverAccountIban);

    TransactionOperationResponseDTO depositMoney(TransactionOperationRequestDTO transactionOperationRequestDTO);

    TransactionOperationResponseDTO withdrawMoney(TransactionOperationRequestDTO transactionOperationRequestDTO);

    TransactionQROperationResponseDTO depositWithQR(TransactionQROperationRequestDTO transactionQROperationRequestDTO) throws JsonProcessingException;

    TransactionQROperationResponseDTO withdrawWithQR(TransactionQROperationRequestDTO transactionQROperationRequestDTO);
}
