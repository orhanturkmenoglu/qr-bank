package com.example.qr_bank.mapper;

import com.example.qr_bank.dto.request.AccountRequestDTO;
import com.example.qr_bank.dto.request.AccountUpdateRequestDTO;
import com.example.qr_bank.dto.response.AccountResponseDTO;
import com.example.qr_bank.model.Account;

import java.util.List;

public interface AccountMapper {

    Account toAccount(AccountRequestDTO accountRequestDTO);

    Account toAccountResponseDTO(AccountResponseDTO accountResponseDTO);

    AccountResponseDTO toAccountResponseDTO(Account account);

    List<AccountResponseDTO> toAccountResponseDTOs(List<Account> accounts);

    AccountUpdateRequestDTO toAccountUpdateRequestDTO(Account account);

    List<Account> toAccounts(List<AccountResponseDTO> accountList);
}
