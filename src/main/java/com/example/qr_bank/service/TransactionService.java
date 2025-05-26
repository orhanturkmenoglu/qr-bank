package com.example.qr_bank.service;


import com.example.qr_bank.dto.request.TransactionOperationRequestDTO;
import com.example.qr_bank.dto.request.TransactionRequestDTO;
import com.example.qr_bank.dto.response.TransactionOperationResponseDTO;
import com.example.qr_bank.dto.response.TransactionResponseDTO;

public interface TransactionService {

    TransactionResponseDTO sendMoney(TransactionRequestDTO transactionRequestDTO);

    TransactionResponseDTO receiveMoney(TransactionRequestDTO transactionRequestDTO);

    TransactionOperationResponseDTO depositMoney(TransactionOperationRequestDTO transactionOperationRequestDTO);

    TransactionOperationResponseDTO withdrawMoney(TransactionOperationRequestDTO transactionOperationRequestDTO);
}
