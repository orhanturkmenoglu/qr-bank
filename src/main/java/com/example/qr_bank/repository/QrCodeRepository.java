package com.example.qr_bank.repository;

import com.example.qr_bank.enums.TransactionType;
import com.example.qr_bank.model.Account;
import com.example.qr_bank.model.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, String> {

    Optional<QrCode> findByAccountAndTransactionTypeAndIsUsedFalse(Account account, TransactionType transactionType);
}