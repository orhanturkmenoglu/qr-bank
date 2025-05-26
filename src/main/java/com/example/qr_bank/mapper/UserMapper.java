package com.example.qr_bank.mapper;

import com.example.qr_bank.dto.request.UserRequestDTO;
import com.example.qr_bank.dto.response.UserResponseDTO;
import com.example.qr_bank.model.User;

import java.util.List;

public interface UserMapper {

    User toUser(UserRequestDTO userRequestDTO);

    UserResponseDTO toUserResponseDTO(User user);

    List<UserResponseDTO> toUserResponseDTOs(List<User> users);
}
