package com.example.qr_bank.service.impl;

import com.example.qr_bank.dto.request.UserRequestDTO;
import com.example.qr_bank.dto.response.UserResponseDTO;
import com.example.qr_bank.exception.UserAlreadyExistsException;
import com.example.qr_bank.exception.UserNotFoundException;
import com.example.qr_bank.mapper.AccountMapper;
import com.example.qr_bank.mapper.UserMapper;
import com.example.qr_bank.model.Account;
import com.example.qr_bank.model.User;
import com.example.qr_bank.repository.UserRepository;
import com.example.qr_bank.service.UserService;
import com.example.qr_bank.utils.GenerateIban;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AccountMapper accountMapper;

    @Transactional
    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        log.info("UserServiceImpl::createUser {}", userRequestDTO);

        if (ObjectUtils.isEmpty(userRequestDTO)) {
            log.error("UserServiceImpl::createUser UserRequestDTO is null");
            throw new NullPointerException("UserRequestDTO is null");
        }

        boolean isEmailExists = userRepository.existsByEmail(userRequestDTO.getEmail());

        if (isEmailExists) {
            log.error("UserServiceImpl::createUser User with email {} already exists", userRequestDTO.getEmail());
            throw new UserAlreadyExistsException("User with email " + userRequestDTO.getEmail() + " already exists");
        }

        User user = userMapper.toUser(userRequestDTO);

        List<Account> accounts = userRequestDTO.getAccountRequestDTOS()
                .stream()
                .map(accountDto -> {
                    Account account = accountMapper.toAccount(accountDto);
                    account.setOwner(user);
                    account.setBalance(BigDecimal.ZERO);
                    account.setIban(GenerateIban.generateIban());
                    account.setCurrency(userRequestDTO.getAccountRequestDTOS().get(0).getCurrency());
                    return account;
                })
                .collect(Collectors.toList());

        user.setAccountList(accounts);

        User savedUser = userRepository.save(user);
        log.info("UserServiceImpl::createUser saved user {}", savedUser);

        log.info("UserServiceImpl::createUser created user {}", savedUser);
        return userMapper.toUserResponseDTO(savedUser);

    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        log.info("UserServiceImpl::getAllUsers");

        List<User> userList = userRepository.findAll();

        if (userList.isEmpty()) {
            log.error("UserServiceImpl::getAllUsers User list is empty");
            throw new NullPointerException("User list is empty");
        }

        return userMapper.toUserResponseDTOs(userList);
    }

    @Override
    public UserResponseDTO getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return userMapper.toUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO updateUser(String id, UserRequestDTO userRequestDTO) {
        log.info("UserServiceImpl::updateUser");

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setEmail(userRequestDTO.getEmail());

        User updatedUser = userRepository.save(user);
        log.info("UserServiceImpl::updateUser updated user {}", updatedUser);

        return userMapper.toUserResponseDTO(updatedUser);
    }

    @Override
    public void deleteByUserId(String id) {
        log.info("UserServiceImpl::deleteByUserId");

        boolean isUserExists = userRepository.existsById(id);

        if (!isUserExists) {
            log.error("UserServiceImpl::deleteByUserId User with id {} not found", id);
            throw new UserNotFoundException("User with id " + id + " not found");
        }

        userRepository.deleteById(id);
    }
}
