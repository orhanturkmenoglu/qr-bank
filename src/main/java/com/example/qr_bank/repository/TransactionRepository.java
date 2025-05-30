package com.example.qr_bank.repository;

import com.example.qr_bank.enums.TransactionType;
import com.example.qr_bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query("""
                  SELECT COALESCE(SUM (t.amount), 0)
                   FROM Transaction  t where t.senderAccount.id=:senderId and
                    t.transactionType=:transactionType and
                    t.createdAt BETWEEN :startOfDay and :endOfDay
            """)
    BigDecimal findTotalTransferredToday(
            @Param("senderId") String senderId,
            @Param("transactionType") TransactionType transactionType,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}