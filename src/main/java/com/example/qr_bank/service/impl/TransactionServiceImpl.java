package com.example.qr_bank.service.impl;

import com.example.qr_bank.dto.request.AccountUpdateRequestDTO;
import com.example.qr_bank.dto.request.TransactionOperationRequestDTO;
import com.example.qr_bank.dto.request.TransactionRequestDTO;
import com.example.qr_bank.dto.response.AccountResponseDTO;
import com.example.qr_bank.dto.response.TransactionOperationResponseDTO;
import com.example.qr_bank.dto.response.TransactionResponseDTO;
import com.example.qr_bank.enums.TransactionStatus;
import com.example.qr_bank.enums.TransactionType;
import com.example.qr_bank.exception.AccountIbanNotFoundException;
import com.example.qr_bank.exception.TransactionNullPointerException;
import com.example.qr_bank.mapper.AccountMapper;
import com.example.qr_bank.mapper.TransactionMapper;
import com.example.qr_bank.model.Account;
import com.example.qr_bank.model.Transaction;
import com.example.qr_bank.repository.AccountRepository;
import com.example.qr_bank.repository.TransactionRepository;
import com.example.qr_bank.service.AccountService;
import com.example.qr_bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final AccountService accountService;
    private final TransactionRepository transactionRepository;
    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;
    private final AccountRepository accountRepository;

    @Transactional
    @Override
    public TransactionResponseDTO sendMoney(TransactionRequestDTO transactionRequestDTO) {
        log.info("TransactionServiceImpl::sendMoney {}", transactionRequestDTO);
/*
        BigDecimal amount = transactionRequestDTO.getAmount();

        AccountResponseDTO senderDto = accountService.getAccountByIban(transactionRequestDTO.getSenderAccountIban());
        AccountResponseDTO receiverDto = accountService.getAccountByIban(transactionRequestDTO.getReceiverAccountIban());

        Account senderAccount = accountMapper.toAccount(senderDto);
        Account receiverAccount = accouaccountMapperntMapperImpl.toAccount(receiverDto);

        if (ObjectUtils.isEmpty(transactionRequestDTO)) {
            log.error("TransactionServiceImpl::sendMoney TransactionRequestDTO is null");
            throw new TransactionNullPointerException("TransactionRequestDTO is null");
        }

        validateIbans(transactionRequestDTO);
        validateDifferentAccounts(transactionRequestDTO);


        if (senderAccount.getBalance().compareTo(amount) < 0) {
            log.error("TransactionServiceImpl::sendMoney Sender account does not have enough balance");
            throw new IllegalArgumentException("Sender account does not have enough balance");
        }

        // hesap güncelle
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        receiverAccount.setBalance(receiverAccount.getBalance().add(amount));


        accountService.updateAccount(senderAccount.getId(), accountMapperImpl.toAccountRequestDto(senderAccount));
        accountService.updateAccount(receiverAccount.getId(), accountMapperImpl.toAccountRequestDto(receiverAccount));

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID().toString())
                .senderAccount(senderAccount)
                .receiverAccount(receiverAccount)
                .status(TransactionStatus.COMPLETED)
                .amount(amount)
                .qrCode(null)
                .build();


        Transaction saveTransaction = transactionRepository.save(transaction);
        log.info("TransactionServiceImpl::sendMoney saved transaction {}", saveTransaction);

        return transactionMapperImpl.toTransactionResponseDTO(saveTransaction);*/

        return null;
    }

    @Override
    public TransactionResponseDTO receiveMoney(TransactionRequestDTO transactionRequestDTO) {
        log.info("TransactionServiceImpl::receiveMoney {}", transactionRequestDTO);

        validateIbans(transactionRequestDTO);

        return null;
    }

    @Override
    public TransactionOperationResponseDTO depositMoney(TransactionOperationRequestDTO transactionOperationDTO) {
        log.info("TransactionServiceImpl::depositMoney {}", transactionOperationDTO);

        if (ObjectUtils.isEmpty(transactionOperationDTO)) {
            log.error("TransactionServiceImpl::depositMoney TransactionRequestDTO is null");
            throw new TransactionNullPointerException("TransactionRequestDTO is null");
        }

        if (transactionOperationDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("TransactionServiceImpl::depositMoney Amount cannot be negative");
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        if (transactionOperationDTO.getAmount().remainder(BigDecimal.TEN).compareTo(BigDecimal.ZERO) != 0) {
            log.error("TransactionServiceImpl::depositMoney Amount must be a multiple of 10");
            throw new IllegalArgumentException("Amount must be a multiple of 10");
        }

        AccountResponseDTO receiverDto = accountService.getAccountByIban(transactionOperationDTO.getAccountIban());

        Account account = accountMapper.toAccountResponseDTO(receiverDto);

        account.setBalance(account.getBalance().add(transactionOperationDTO.getAmount()));

        accountService.updateAccount(receiverDto.getId(), accountMapper.toAccountUpdateRequestDTO(account));

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID().toString())
                .senderAccount(null)
                .receiverAccount(account)
                .status(TransactionStatus.COMPLETED)
                .transactionType(TransactionType.DEPOSIT)
                .amount(transactionOperationDTO.getAmount())
                .description(transactionOperationDTO.getDescription())
                .qrCode(null)
                .build();

        Transaction saved = transactionRepository.save(transaction);
        log.info("TransactionServiceImpl::depositMoney saved transaction {}", saved);

        return TransactionOperationResponseDTO.builder()
                .timestamp(saved.getCreatedAt())
                .accountIban(transactionOperationDTO.getAccountIban())
                .amount(transactionOperationDTO.getAmount())
                .status(TransactionStatus.COMPLETED)
                .transactionType(transactionOperationDTO.getTransactionType())
                .description(transactionOperationDTO.getDescription())
                .build();
    }

    @Override
    public TransactionOperationResponseDTO withdrawMoney(TransactionOperationRequestDTO transactionRequestDTO) {
        return null;
    }

    private void validateIbans(TransactionRequestDTO dto) {
        if (!accountService.existsAccountByIban(dto.getSenderAccountIban())) {
            throw new AccountIbanNotFoundException("Sender IBAN not found");
        }

    }

    private void validateDifferentAccounts(TransactionRequestDTO dto) {
        if (dto.getSenderAccountIban().equals(dto.getReceiverAccountIban())) {
            throw new IllegalArgumentException("Sender and receiver IBAN cannot be the same");
        }
    }

}
