package com.example.qr_bank.service.impl;

import com.example.qr_bank.dto.request.QrCodeRequestDTO;
import com.example.qr_bank.dto.request.TransactionOperationRequestDTO;
import com.example.qr_bank.dto.request.TransactionQROperationRequestDTO;
import com.example.qr_bank.dto.request.TransactionRequestDTO;
import com.example.qr_bank.dto.response.AccountResponseDTO;
import com.example.qr_bank.dto.response.TransactionOperationResponseDTO;
import com.example.qr_bank.dto.response.TransactionQROperationResponseDTO;
import com.example.qr_bank.dto.response.TransactionResponseDTO;
import com.example.qr_bank.enums.TransactionStatus;
import com.example.qr_bank.enums.TransactionType;
import com.example.qr_bank.exception.TransactionNullPointerException;
import com.example.qr_bank.mapper.AccountMapper;
import com.example.qr_bank.mapper.TransactionMapper;
import com.example.qr_bank.model.Account;
import com.example.qr_bank.model.QrCode;
import com.example.qr_bank.model.Transaction;
import com.example.qr_bank.repository.QrCodeRepository;
import com.example.qr_bank.repository.TransactionRepository;
import com.example.qr_bank.service.AccountService;
import com.example.qr_bank.service.QrCodeService;
import com.example.qr_bank.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final AccountService accountService;
    private final TransactionRepository transactionRepository;
    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;
    private final QrCodeService qrCodeService;
    private final QrCodeRepository qrCodeRepository;

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

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Transaction  ServiceImpl::sendMoney Amount cannot be negative");
            throw new IllegalArgumentException("Amount cannot be negative");
        }


        TransactionType transactionType = transactionRequestDTO.getTransactionType();

        if (!(transactionType == TransactionType.TRANSFER || transactionType == TransactionType.EFT || transactionType == TransactionType.HAVALE ||
                transactionType == TransactionType.QR_SEND)) {
            throw new IllegalArgumentException("Invalid transaction type for sendMoney");
        }

        AccountResponseDTO senderResponseDTO = accountService.getAccountByIban(senderAccountIban);
        AccountResponseDTO receiverResponseDTO = accountService.getAccountByIban(receiverAccountIban);

        Account sender = accountMapper.toAccountResponseDTO(senderResponseDTO);
        Account receiver = accountMapper.toAccountResponseDTO(receiverResponseDTO);

        if (sender.getBalance().compareTo(amount) < 0) {
            log.error("TransactionServiceImpl::sendMoney Sender balance is not enough");
            throw new IllegalArgumentException("Sender balance is not enough");
        }

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        BigDecimal totalTransferredToday = transactionRepository.findTotalTransferredToday(sender.getId(),
                transactionRequestDTO.getTransactionType(), startOfDay, endOfDay);

        if (totalTransferredToday.add(amount).compareTo(sender.getDailyLimit()) > 0) {
            log.error("TransactionServiceImpl::sendMoney Sender daily limit exceeded");
            throw new IllegalArgumentException("Sender daily limit exceeded");
        }

        sender.setBalance(sender.getBalance().subtract(amount));

        accountService.updateAccount(sender.getId(), accountMapper.toAccountUpdateRequestDTO(sender));

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID().toString())
                .receiverAccount(receiver)
                .senderAccount(sender)
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

        // IBAN boş mu?
        if (!StringUtils.hasText(receiverAccountIban)) {
            log.error("Receiver IBAN cannot be empty");
            throw new IllegalArgumentException("Receiver IBAN cannot be empty");
        }

        //  Transaction'ı kontrol et
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalStateException("Transaction not found"));

        // Transaction durumu PENDING mi?
        if (!transaction.getStatus().equals(TransactionStatus.PENDING)) {
            log.error("Transaction status is not PENDING. Current status: {}", transaction.getStatus());
            throw new IllegalStateException("Transaction status must be PENDING to receive money");
        }

        // IBAN eşleşiyor mu?
        if (!transaction.getReceiverAccount().getIban().equals(receiverAccountIban)) {
            log.error("Receiver IBAN mismatch. Expected: {}, Provided: {}", transaction.getReceiverAccount().getIban(), receiverAccountIban);
            throw new IllegalArgumentException("Receiver IBAN mismatch");
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

        AccountResponseDTO accountResponseDTO = accountService.getAccountByIban(transactionOperationDTO.getAccountIban());

        Account account = accountMapper.toAccountResponseDTO(accountResponseDTO);

        account.setBalance(account.getBalance().add(transactionOperationDTO.getAmount()));

        accountService.updateAccount(accountResponseDTO.getId(), accountMapper.toAccountUpdateRequestDTO(account));

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID().toString())
                .senderAccount(account)
                .receiverAccount(null)
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

    @Override
    public TransactionQROperationResponseDTO depositWithQR(TransactionQROperationRequestDTO requestDTO) throws JsonProcessingException {
        log.info("TransactionServiceImpl::depositWithQR {}", requestDTO);

        validateDepositRequest(requestDTO);

        // QR kodu decode ve doğrulama
        QrCodeRequestDTO qrCodeRequestDTO = decodeAndValidateQrCode(requestDTO.getQrCode());


        if (qrCodeRequestDTO.getTransactionType().equals(TransactionType.QR_SEND)) {
            log.error("TransactionServiceImpl::depositWithQR Invalid transaction type for depositWithQR");
            throw new IllegalArgumentException("Invalid transaction type for depositWithQR");
        }

        // QR kodundaki IBAN'dan hesap getir
        Account account = getAccountFromQrCode(qrCodeRequestDTO);

        // Aktif QR kod bul
        QrCode qrCode = findValidQrCode(account, qrCodeRequestDTO.getTransactionType());

        // Hesap bakiyesini artır
        updateAccountBalance(account, qrCodeRequestDTO.getAmount(), account.getId());

        // Transaction oluştur
        Transaction transaction = getTransaction(account, qrCode);

        Transaction saved = transactionRepository.save(transaction);

        log.info("TransactionServiceImpl::depositWithQR completed for transactionId: {}", saved.getId());

        markQrCodeAsUsed(qrCode);

        return TransactionQROperationResponseDTO.builder()
                .transactionId(saved.getId())
                .transactionType(saved.getTransactionType())
                .status(saved.getStatus())
                .timestamp(saved.getCreatedAt())
                .build();
    }

    @Override
    public TransactionQROperationResponseDTO withdrawWithQR(TransactionQROperationRequestDTO requestDTO) {
        log.info("TransactionServiceImpl::withdrawWithQR {}", requestDTO);

        validateDepositRequest(requestDTO);

        QrCodeRequestDTO qrCodeRequestDTO = decodeAndValidateQrCode(requestDTO.getQrCode());

        if (!qrCodeRequestDTO.getTransactionType().equals(TransactionType.QR_WITHDRAWAL)) {
            log.error("Invalid transaction type for withdrawWithQR");
            throw new IllegalArgumentException("Invalid transaction type for withdrawWithQR");
        }

        Account account = getAccountFromQrCode(qrCodeRequestDTO);

        QrCode qrCode = findValidQrCode(account, qrCodeRequestDTO.getTransactionType());

        validateBalanceForWithdrawal(account, qrCodeRequestDTO.getAmount());

        account.setBalance(account.getBalance().subtract(qrCodeRequestDTO.getAmount()));
        accountService.updateAccount(account.getId(), accountMapper.toAccountUpdateRequestDTO(account));

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID().toString())
                .senderAccount(account)
                .receiverAccount(null)
                .status(TransactionStatus.COMPLETED)
                .transactionType(TransactionType.QR_WITHDRAWAL)
                .amount(qrCodeRequestDTO.getAmount())
                .description(qrCodeRequestDTO.getDescription())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        markQrCodeAsUsed(qrCode);

        return buildTransactionResponse(savedTransaction);
    }



    private void validateDepositRequest(TransactionQROperationRequestDTO request) {
        if (ObjectUtils.isEmpty(request)) {
            throw new TransactionNullPointerException("TransactionRequestDTO is null");
        }
    }

    private QrCode findValidQrCode(Account account, TransactionType transactionType) {
        return qrCodeRepository.findByAccountAndTransactionTypeAndIsUsedFalse(account, transactionType)
                .filter(qr -> qr.getExpirationDate().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new IllegalArgumentException("Valid or non-expired QR code not found for this account"));
    }

    private void updateAccountBalance(Account account, BigDecimal amount, String accountId) {
        account.setBalance(account.getBalance().add(amount));
        accountService.updateAccount(accountId, accountMapper.toAccountUpdateRequestDTO(account));
    }

    private Transaction getTransaction(Account account, QrCode qrCode) {
        return Transaction.builder()
                .id(UUID.randomUUID().toString())
                .senderAccount(null)
                .receiverAccount(account)
                .amount(qrCode.getAmount())
                .status(TransactionStatus.COMPLETED)
                .transactionType(TransactionType.QR_DEPOSIT)
                .qrCode(qrCode)
                .build();
    }

    private void markQrCodeAsUsed(QrCode qrCode) {
        qrCode.setUsed(true);
        qrCodeRepository.save(qrCode);
    }

    private TransactionQROperationResponseDTO buildTransactionResponse(Transaction transaction) {
        return TransactionQROperationResponseDTO.builder()
                .transactionId(transaction.getId())
                .timestamp(transaction.getCreatedAt())
                .status(TransactionStatus.COMPLETED)
                .transactionType(transaction.getTransactionType())
                .build();
    }

    private QrCodeRequestDTO decodeAndValidateQrCode(String base64QrCode) {
        if (base64QrCode == null || base64QrCode.isEmpty()) {
            log.error("QRCode field is missing or empty");
            throw new IllegalArgumentException("QRCode field is missing or empty");
        }

        byte[] qrCodeBytes = Base64.getDecoder().decode(base64QrCode.getBytes(StandardCharsets.UTF_8));
        log.info("Decoded QR code bytes: {}", qrCodeBytes);

        QrCodeRequestDTO qrCodeRequestDTO = qrCodeService.decodeQrCode(qrCodeBytes);
        log.info("Decoded QR code DTO: {}", qrCodeRequestDTO);

        if (ObjectUtils.isEmpty(qrCodeRequestDTO)) {
            log.error("QRCode is invalid");
            throw new IllegalArgumentException("QRCode is invalid");
        }

        if (qrCodeRequestDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Amount cannot be negative");
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        if (qrCodeRequestDTO.getAmount().remainder(BigDecimal.TEN).compareTo(BigDecimal.ZERO) != 0) {
            log.error("Amount must be a multiple of 10");
            throw new IllegalArgumentException("Amount must be a multiple of 10");
        }

        return qrCodeRequestDTO;
    }

    private Account getAccountFromQrCode(QrCodeRequestDTO qrCodeRequestDTO) {
        AccountResponseDTO accountResponseDTO = accountService.getAccountByIban(qrCodeRequestDTO.getAccountIban());
        return accountMapper.toAccountResponseDTO(accountResponseDTO);
    }

    private void validateBalanceForWithdrawal(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            log.error("Insufficient balance for withdrawal");
            throw new IllegalArgumentException("Insufficient balance");
        }
    }
}
