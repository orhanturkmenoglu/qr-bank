package com.example.qr_bank.model;

import com.example.qr_bank.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "qr_codes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QrCode extends BaseEntity {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private LocalDateTime expirationDate;

    private boolean isUsed;

    @Enumerated(EnumType.STRING)
   private TransactionType transactionType;
}
