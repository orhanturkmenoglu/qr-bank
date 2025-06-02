package com.example.qr_bank.service;

import com.example.qr_bank.dto.request.AccountUpdateRequestDTO;
import com.example.qr_bank.dto.response.AccountResponseDTO;
import com.example.qr_bank.model.User;

import java.util.List;

public interface AccountService {

    AccountResponseDTO getAccountById(String id);

    List<AccountResponseDTO> getAllAccountsByUserId(String userId);

    AccountResponseDTO updateAccount(String id, AccountUpdateRequestDTO accountUpdateRequestDTO);

    void deleteAccountById(String id);

    boolean existsAccountByIban(String senderAccountIban);

    AccountResponseDTO getAccountByIban(String iban);

    AccountResponseDTO getAccountByOwner(User owner);

}
