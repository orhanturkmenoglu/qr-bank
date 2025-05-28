package com.example.qr_bank.service;

import com.example.qr_bank.dto.request.UserRequestDTO;
import com.example.qr_bank.dto.response.UserResponseDTO;

import java.util.List;

public interface UserService {

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(String id);

    UserResponseDTO updateUser(String id, UserRequestDTO userRequestDTO);

    void deleteByUserId(String id);
}
