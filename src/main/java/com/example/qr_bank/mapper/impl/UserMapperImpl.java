package com.example.qr_bank.mapper.impl;

import com.example.qr_bank.dto.request.UserRequestDTO;
import com.example.qr_bank.dto.response.AccountResponseDTO;
import com.example.qr_bank.dto.response.UserResponseDTO;
import com.example.qr_bank.enums.Role;
import com.example.qr_bank.mapper.AccountMapper;
import com.example.qr_bank.mapper.UserMapper;
import com.example.qr_bank.model.Account;
import com.example.qr_bank.model.User;
import com.example.qr_bank.utils.AESEncryptionUtil;
import com.example.qr_bank.utils.IbanGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class UserMapperImpl implements UserMapper {

    private final AccountMapper accountMapper;
    private final AESEncryptionUtil encryptionUtil;

    @Override
    public User toUser(UserRequestDTO userRequestDTO) {

        if (userRequestDTO == null) {
            return null;
        }

        List<Account> accounts = userRequestDTO.getAccountRequestDTOS()
                .stream()
                .map(accountDto -> {
                    Account account = accountMapper.toAccount(accountDto);
                    account.setBalance(BigDecimal.ZERO);
                    account.setIban(encryptionUtil.encrypt(IbanGenerator.generateIban()));
                    account.setCurrency(userRequestDTO.getAccountRequestDTOS().get(0).getCurrency());
                    return account;
                })
                .toList();

        return User.builder()
                .id(UUID.randomUUID().toString())
                .identityNumber(userRequestDTO.getIdentityNumber())
                .firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .email(userRequestDTO.getEmail())
                .password(userRequestDTO.getPassword())
                .telephoneNumber(userRequestDTO.getTelephoneNumber())
                .accountList(accounts)
                .role(Role.ROLE_USER)
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
                .identityNumber(user.getIdentityNumber())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .telephoneNumber(user.getTelephoneNumber())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .accountList(accountMapper.toAccountResponseDTOs(accountList))
                .build();
    }

    @Override
    public User toUser(UserResponseDTO userResponseDTO) {
        if (userResponseDTO == null) {
            return null;
        }

        List<AccountResponseDTO> accountList = userResponseDTO.getAccountList();
        List<Account> accounts = accountMapper.toAccounts(accountList);
        return User.builder()
                .id(userResponseDTO.getId())
                .identityNumber(userResponseDTO.getIdentityNumber())
                .firstName(userResponseDTO.getFirstName())
                .lastName(userResponseDTO.getLastName())
                .email(userResponseDTO.getEmail())
                .telephoneNumber(userResponseDTO.getTelephoneNumber())
                .accountList(accounts)
                // Diğer User özellikleri DTO'da yoksa varsayılanlar atanabilir.
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
