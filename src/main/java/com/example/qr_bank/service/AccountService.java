package com.example.qr_bank.service;

import com.example.qr_bank.dto.request.AccountRequestDTO;
import com.example.qr_bank.dto.response.AccountResponseDTO;

import java.util.List;

public interface AccountService {

    AccountResponseDTO createAccount( String userId,AccountRequestDTO accountRequestDTO);

    AccountResponseDTO getAccountById(String id);

    List<AccountResponseDTO> getAllAccountsByUserId(String userId);

    AccountResponseDTO updateAccount(String id, AccountRequestDTO accountRequestDTO);

    void deleteAccountById(String id);
}
