package com.example.qr_bank.mapper;

import com.example.qr_bank.dto.request.TransactionRequestDTO;
import com.example.qr_bank.dto.response.TransactionResponseDTO;
import com.example.qr_bank.model.Transaction;

import java.util.List;

public interface TransactionMapper {

    Transaction toTransaction(TransactionRequestDTO transactionRequestDTO);

    TransactionResponseDTO toTransactionResponseDTO(Transaction transaction);

    List<TransactionResponseDTO> toTransactionResponseDTOs(List<Transaction> transactions);
}
