package com.example.qr_bank.service.impl;

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
import org.springframework.util.StringUtils;

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

    @Transactional
    @Override
    public TransactionResponseDTO sendMoney(TransactionRequestDTO transactionRequestDTO) {
        log.info("TransactionServiceImpl::sendMoney {}", transactionRequestDTO);

        BigDecimal amount = transactionRequestDTO.getAmount();
        String senderAccountIban = transactionRequestDTO.getSenderAccountIban();
        String receiverAccountIban = transactionRequestDTO.getReceiverAccountIban();

        if (ObjectUtils.isEmpty(transactionRequestDTO)) {
            log.error("TransactionServiceImpl::sendMoney TransactionRequestDTO is null");
            throw new TransactionNullPointerException("TransactionRequestDTO is null");
        }

        if (!accountService.existsAccountByIban(transactionRequestDTO.getSenderAccountIban())) {
            throw new AccountIbanNotFoundException("Sender account IBAN not found");
        }

        TransactionType transactionType = transactionRequestDTO.getTransactionType();

        if (!transactionType.equals(TransactionType.TRANSFER)) {
            log.error("TransactionServiceImpl::sendMoney Transaction type must be TRANSFER");
            throw new IllegalArgumentException("Transaction type must be TRANSFER");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Transaction  ServiceImpl::sendMoney Amount cannot be negative");
            throw new IllegalArgumentException("Amount cannot be negative");
        }


        AccountResponseDTO senderResponseDTO = accountService.getAccountByIban(senderAccountIban);
        AccountResponseDTO receiverResponseDTO = accountService.getAccountByIban(receiverAccountIban);

        Account sender = accountMapper.toAccountResponseDTO(senderResponseDTO);
        Account receiver = accountMapper.toAccountResponseDTO(receiverResponseDTO);

        if (sender.getBalance().compareTo(amount) < 0) {
            log.error("TransactionServiceImpl::sendMoney Sender balance is not enough");
            throw new IllegalArgumentException("Sender balance is not enough");
        }

        sender.setBalance(sender.getBalance().subtract(amount));

        accountService.updateAccount(sender.getId(), accountMapper.toAccountUpdateRequestDTO(sender));

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID().toString())
                .senderAccount(sender)
                .receiverAccount(receiver)
                .amount(amount)
                .transactionType(transactionType)
                .description(transactionRequestDTO.getDescription())
                .status(TransactionStatus.PENDING)
                .qrCode(null)
                .build();

        Transaction saved = transactionRepository.save(transaction);

        log.info("TransactionServiceImpl::sendMoney Transaction saved {}", saved);

        return transactionMapper.toTransactionResponseDTO(saved);
    }

    @Override
    public TransactionResponseDTO receiverMoney(String transactionId,
                                                String receiverAccountIban) {

        log.info("TransactionServiceImpl::receiveMoney transactionId: {}", transactionId);
        log.info("TransactionServiceImpl::receiveMoney receiverAccountIban: {}", receiverAccountIban);

        //  Transaction'ı kontrol et
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalStateException("Transaction not found"));

        // IBAN boş mu?
        if (!StringUtils.hasText(receiverAccountIban)){
            log.error("Receiver IBAN cannot be empty");
            throw new IllegalArgumentException("Receiver IBAN cannot be empty");
        }

        // IBAN eşleşiyor mu?
        if (!transaction.getReceiverAccount().getIban().equals(receiverAccountIban)){
            log.error("Receiver IBAN mismatch. Expected: {}, Provided: {}", transaction.getReceiverAccount().getIban(), receiverAccountIban);
            throw new IllegalArgumentException("Receiver IBAN mismatch");
        }

        // Transaction durumu PENDING mi?
        if (!transaction.getStatus().equals(TransactionStatus.PENDING)){
            log.error("Transaction status is not PENDING. Current status: {}", transaction.getStatus());
            throw new IllegalStateException("Transaction status must be PENDING to receive money");
        }

        //  Alıcı hesabı getir
        AccountResponseDTO receiverResponse = accountService.getAccountByIban(receiverAccountIban);
        Account receiver = accountMapper.toAccountResponseDTO(receiverResponse);

        //  Alıcı bakiyesine parayı ekle
        receiver.setBalance(receiver.getBalance().add(transaction.getAmount()));
        accountService.updateAccount(receiver.getId(), accountMapper.toAccountUpdateRequestDTO(receiver));

        // Transaction durumunu güncelle
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setReceiverAccount(receiver);
        transactionRepository.save(transaction);

        log.info("TransactionServiceImpl::receiveMoney completed successfully for transactionId: {}", transactionId);
        return transactionMapper.toTransactionResponseDTO(transaction);
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
       log.info("TransactionServiceImpl::withdrawMoney {}", transactionRequestDTO);

        if (ObjectUtils.isEmpty(transactionRequestDTO)) {
            log.error("TransactionServiceImpl::withdrawMoney TransactionRequestDTO is null");
            throw new TransactionNullPointerException("TransactionRequestDTO is null");
        }

        if (transactionRequestDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("TransactionServiceImpl::withdrawMoney Amount cannot be negative");
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        if (transactionRequestDTO.getAmount().remainder(BigDecimal.TEN).compareTo(BigDecimal.ZERO) != 0) {
            log.error("TransactionServiceImpl::withdrawMoney Amount must be a multiple of 10");
            throw new IllegalArgumentException("Amount must be a multiple of 10");
        }

        String accountIban = transactionRequestDTO.getAccountIban();

        AccountResponseDTO accountResponseDTO = accountService.getAccountByIban(accountIban);
        Account account = accountMapper.toAccountResponseDTO(accountResponseDTO);

        log.info("TransactionServiceImpl::withdrawMoney account {}", account);
        account.setBalance(account.getBalance().subtract(transactionRequestDTO.getAmount()));

        accountService.updateAccount(accountResponseDTO.getId(),
                accountMapper.toAccountUpdateRequestDTO(account));

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID().toString())
                .senderAccount(null)
                .receiverAccount(account)
                .status(TransactionStatus.COMPLETED)
                .transactionType(TransactionType.WITHDRAWAL)
                .amount(transactionRequestDTO.getAmount())
                .description(transactionRequestDTO.getDescription())
                .qrCode(null)
                .build();

        Transaction saved = transactionRepository.save(transaction);

        return TransactionOperationResponseDTO.builder()
                .timestamp(saved.getCreatedAt())
                .accountIban(accountIban)
                .amount(transactionRequestDTO.getAmount())
                .status(TransactionStatus.COMPLETED)
                .transactionType(transactionRequestDTO.getTransactionType())
                .description(transactionRequestDTO.getDescription())
                .build();
    }


}
