package com.example.qr_bank.model;

import com.example.qr_bank.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "qr_codes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QrCode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private LocalDateTime expirationDate;

    private boolean isUsed;

    private BigDecimal amount;

    private String description;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
}
