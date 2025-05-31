package com.example.qr_bank.mapper.impl;

import com.example.qr_bank.dto.request.TransactionRequestDTO;
import com.example.qr_bank.dto.response.TransactionResponseDTO;
import com.example.qr_bank.mapper.TransactionMapper;
import com.example.qr_bank.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public Transaction toTransaction(TransactionRequestDTO transactionRequestDTO) {

        if (transactionRequestDTO == null) {
            return null;
        }

        return Transaction.builder()
                .amount(transactionRequestDTO.getAmount())
                .description(transactionRequestDTO.getDescription())
                .build();
    }

    @Override
    public TransactionResponseDTO toTransactionResponseDTO(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        String senderIban = transaction.getSenderAccount() != null
                ? transaction.getSenderAccount().getIban()
                : null;

        String receiverIban = transaction.getReceiverAccount() != null
                ? transaction.getReceiverAccount().getIban()
                : null;

        return TransactionResponseDTO.builder()
                .id(transaction.getId())
                .senderAccountIban(senderIban)
                .receiverAccountIban(receiverIban)
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .description(transaction.getDescription())
                .build();
    }

    @Override
    public List<TransactionResponseDTO> toTransactionResponseDTOs(List<Transaction> transactions) {
        if (transactions == null) {
            return null;
        }
        return transactions.stream()
                .map(this::toTransactionResponseDTO)
                .toList();
    }
}
