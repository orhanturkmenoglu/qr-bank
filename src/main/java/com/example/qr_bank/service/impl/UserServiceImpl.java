package com.example.qr_bank.service.impl;

import com.example.qr_bank.dto.request.UserRequestDTO;
import com.example.qr_bank.dto.response.UserResponseDTO;
import com.example.qr_bank.exception.UserNotFoundException;
import com.example.qr_bank.mapper.UserMapper;
import com.example.qr_bank.model.User;
import com.example.qr_bank.repository.UserRepository;
import com.example.qr_bank.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


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

    @Override
    public boolean existsByIdentificationNumber(String identificationNumber) {
        log.info("UserServiceImpl::existsByIdentificationNumber");

        boolean existsByIdentityNumber = userRepository.existsByIdentityNumber(identificationNumber);

        if (!existsByIdentityNumber) {
            log.error("UserServiceImpl::existsByIdentificationNumber User with identity number {} not found", identificationNumber);
            throw new UserNotFoundException("User with identity number " + identificationNumber + " not found");
        }
        return true;
    }

    @Override
    public boolean existsByEmail(String email) {
        log.info("UserServiceImpl::existsByEmail");

        boolean existsByEmail = userRepository.existsByEmail(email);

        if (!existsByEmail) {
            log.error("UserServiceImpl::existsByEmail User with email {} not found", email);
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        return true;
    }

    @Override
    public boolean existsByTelephoneNumber(String telephoneNumber) {
        log.info("UserServiceImpl::existsByTelephoneNumber");

        boolean existsByTelephoneNumber = userRepository.existsByTelephoneNumber(telephoneNumber);

        if (!existsByTelephoneNumber) {
            log.error("UserServiceImpl::existsByTelephoneNumber User with telephone number {} not found", telephoneNumber);
            throw new UserNotFoundException("User with telephone number " + telephoneNumber + " not found");
        }
        return true;
    }

    @Override
    public UserResponseDTO getUserByTelephoneNumber(String phoneNumber) {
        log.info("UserServiceImpl::getUserByTelephoneNumber");

        User user = userRepository.findByTelephoneNumber(phoneNumber)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return userMapper.toUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        log.info("UserServiceImpl::getUserByEmail");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return userMapper.toUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO getUserByIdentificationNumber(String identificationNumber) {
        log.info("UserServiceImpl::getUserByIdentificationNumber");
        User user = userRepository.findByIdentificationNumber(identificationNumber)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return userMapper.toUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO getCurrentUser() {
        log.info("UserServiceImpl:getCurrentUser");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("UserServiceImpl::getCurrentUser User is not authenticated");
            throw new UserNotFoundException("User is not authenticated");
        }

        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username :" + username));

        return userMapper.toUserResponseDTO(user);
    }
}
