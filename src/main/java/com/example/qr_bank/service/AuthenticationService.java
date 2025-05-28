package com.example.qr_bank.service;

import com.example.qr_bank.dto.request.LoginRequestDTO;
import com.example.qr_bank.dto.request.UpdatePasswordRequestDto;
import com.example.qr_bank.dto.request.UserRequestDTO;
import com.example.qr_bank.dto.response.LoginResponseDTO;
import com.example.qr_bank.dto.response.UserResponseDTO;

public interface AuthenticationService {

    UserResponseDTO registerUser(UserRequestDTO userRequestDTO);

    LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO);

    String updatePassword(UpdatePasswordRequestDto updatePasswordRequestDto);
}
