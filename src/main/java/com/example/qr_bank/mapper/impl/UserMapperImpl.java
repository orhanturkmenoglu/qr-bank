package com.example.qr_bank.mapper.impl;

import com.example.qr_bank.dto.request.AccountRequestDTO;
import com.example.qr_bank.dto.request.UserRequestDTO;
import com.example.qr_bank.dto.response.UserResponseDTO;
import com.example.qr_bank.enums.Role;
import com.example.qr_bank.mapper.AccountMapper;
import com.example.qr_bank.mapper.UserMapper;
import com.example.qr_bank.model.Account;
import com.example.qr_bank.model.User;
import com.example.qr_bank.utils.GenerateIban;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final AccountMapper accountMapper;

    @Override
    public User toUser(UserRequestDTO userRequestDTO) {

        if (userRequestDTO == null) {
            return null;
        }


        return User.builder()
                .id(UUID.randomUUID().toString())
                .firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .email(userRequestDTO.getEmail())
                .password(userRequestDTO.getPassword())
                .role(Role.USER)
                .build();
    }

    @Override
    public UserResponseDTO toUserResponseDTO(User user) {

        if (user == null) {
            return null;
        }

        List<Account> accountList = user.getAccountList();

        for (Account account : accountList) {
            account.setOwner(user);
            account.setCurrency(user.getAccountList().get(0).getCurrency());
            account.setQrCodes(null);
            account.setSentTransactions(null);
            account.setReceivedTransactions(null);
        }

        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .accountList(accountMapper.toAccountResponseDTOs(accountList))
                .build();
    }

    @Override
    public List<UserResponseDTO> toUserResponseDTOs(List<User> users) {

        if (users == null) {
            return null;
        }
        return users.stream()
                .map(this::toUserResponseDTO).toList();
    }
}
