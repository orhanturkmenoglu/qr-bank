package com.example.qr_bank.service.impl;

import com.example.qr_bank.dto.response.AccountResponseDTO;
import com.example.qr_bank.dto.response.TransactionResponseDTO;
import com.example.qr_bank.dto.response.UserResponseDTO;
import com.example.qr_bank.enums.Role;
import com.example.qr_bank.mapper.UserMapper;
import com.example.qr_bank.service.AccountService;
import com.example.qr_bank.service.AdminService;
import com.example.qr_bank.service.TransactionService;
import com.example.qr_bank.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AccountService accountService;
    private final TransactionService transactionService;

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public UserResponseDTO updateUserRoleById(String id, Role role) {
        UserResponseDTO userResponseDTO = userService.getUserById(id);
        return userResponseDTO;
    }

    @Override
    public List<AccountResponseDTO> getAllAccounts() {
        return List.of();
    }

    @Override
    public List<TransactionResponseDTO> getAllTransactions() {
        return List.of();
    }

}
