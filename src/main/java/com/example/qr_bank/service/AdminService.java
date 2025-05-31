package com.example.qr_bank.service;

import com.example.qr_bank.dto.response.AccountResponseDTO;
import com.example.qr_bank.dto.response.TransactionResponseDTO;
import com.example.qr_bank.dto.response.UserResponseDTO;
import com.example.qr_bank.enums.Role;

import java.util.List;

public interface AdminService {
    List<UserResponseDTO> getAllUsers() ;

    UserResponseDTO updateUserRoleById(String id, Role role);

    List<AccountResponseDTO> getAllAccounts();

    List<TransactionResponseDTO> getAllTransactions();
}
