package com.example.qr_bank.model;

import com.example.qr_bank.enums.EasyAddressType;
import com.example.qr_bank.enums.TransactionStatus;
import com.example.qr_bank.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends BaseEntity {

    @Id
    private String id;

    @ManyToOne
    private Account senderAccount;

    @ManyToOne
    private Account receiverAccount;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private EasyAddressType easyAddressType;

    private String recipientIdentifier;

    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qr_code_id")
    private QrCode qrCode;

}
