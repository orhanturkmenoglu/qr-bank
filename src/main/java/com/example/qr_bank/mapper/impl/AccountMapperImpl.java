package com.example.qr_bank.mapper.impl;

import com.example.qr_bank.dto.request.AccountRequestDTO;
import com.example.qr_bank.dto.request.AccountUpdateRequestDTO;
import com.example.qr_bank.dto.response.AccountResponseDTO;
import com.example.qr_bank.mapper.AccountMapper;
import com.example.qr_bank.model.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AccountMapperImpl implements AccountMapper {


    @Override
    public Account toAccount(AccountRequestDTO accountRequestDTO) {

        if (accountRequestDTO == null) {
            return null;
        }

        return Account.builder()
                .currency(accountRequestDTO.getCurrency())
                .dailyLimit(BigDecimal.valueOf(25000))
                .build();
    }

    @Override
    public Account toAccountResponseDTO(AccountResponseDTO accountResponseDTO) {

        if (accountResponseDTO == null) {
            return null;
        }

        return Account.builder()
                .id(accountResponseDTO.getId())
                .iban(accountResponseDTO.getIban())
                .balance(accountResponseDTO.getBalance())
                .currency(accountResponseDTO.getCurrency())
                .dailyLimit(BigDecimal.valueOf(10000))
                .build();
    }

    @Override
    public AccountResponseDTO toAccountResponseDTO(Account account) {

        if (account == null) {
            return null;
        }

        return AccountResponseDTO.builder()
                .id(account.getId())
                .iban(account.getIban())
                .balance(account.getBalance())
                .dailyLimit(account.getDailyLimit())
                .currency(account.getCurrency())
                .ownerId(account.getOwner().getId())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();

    }

    @Override
    public List<AccountResponseDTO> toAccountResponseDTOs(List<Account> accounts) {

        if (accounts == null) {
            return null;
        }

        return accounts.stream()
                .map(this::toAccountResponseDTO)
                .toList();
    }

    @Override
    public AccountUpdateRequestDTO toAccountUpdateRequestDTO(Account account) {

        if (account == null) {
            return null;
        }

        return AccountUpdateRequestDTO.builder()
                .currencyType(account.getCurrency())
                .balance(account.getBalance())
                .build();
    }

    @Override
    public List<Account> toAccounts(List<AccountResponseDTO> accountList) {
        if (accountList == null) {
            return null;
        }

        return accountList.stream()
                .map(this::toAccountResponseDTO)
                .toList();
    }

}
