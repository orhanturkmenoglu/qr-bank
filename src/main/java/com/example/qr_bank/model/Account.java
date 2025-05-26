package com.example.qr_bank.model;

import com.example.qr_bank.enums.CurrencyType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String iban;

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "senderAccount")
    private List<Transaction> sentTransactions;

    @OneToMany(mappedBy = "receiverAccount")
    private List<Transaction> receivedTransactions;

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    private List<QrCode> qrCodes;

}
