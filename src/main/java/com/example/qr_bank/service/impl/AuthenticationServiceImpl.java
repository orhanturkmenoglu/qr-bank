package com.example.qr_bank.service.impl;

import com.example.qr_bank.dto.request.LoginRequestDTO;
import com.example.qr_bank.dto.request.UpdatePasswordRequestDto;
import com.example.qr_bank.dto.request.UserRequestDTO;
import com.example.qr_bank.dto.response.LoginResponseDTO;
import com.example.qr_bank.dto.response.UserResponseDTO;
import com.example.qr_bank.exception.UserAlreadyExistsException;
import com.example.qr_bank.exception.UserNotFoundException;
import com.example.qr_bank.mapper.UserMapper;
import com.example.qr_bank.model.User;
import com.example.qr_bank.repository.UserRepository;
import com.example.qr_bank.security.JwtTokenUtil;
import com.example.qr_bank.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;

    @Transactional
    @Override
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        log.info("AuthenticationServiceImpl::registerUser {}", userRequestDTO);

        if (ObjectUtils.isEmpty(userRequestDTO)) {
            log.error("AuthenticationServiceImpl::registerUser UserRequestDTO is empty");
            throw new UserNotFoundException("Register User Request is empty");
        }

        boolean isEmailExists = userRepository.existsByEmail(userRequestDTO.getEmail());

        if (isEmailExists) {
            log.error("UserServiceImpl::createUser User with email {} already exists", userRequestDTO.getEmail());
            throw new UserAlreadyExistsException("User with email " + userRequestDTO.getEmail() + " already exists");
        }

        User user = userMapper.toUser(userRequestDTO);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        log.info("UserServiceImpl::createUser saved user {}", savedUser);

        log.info("UserServiceImpl::createUser created user {}", savedUser);
        return userMapper.toUserResponseDTO(savedUser);

    }

    @Override
    public LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {
        log.info("AuthenticationServiceImpl::loginUser {}", loginRequestDTO);

        if (loginRequestDTO.getEmail() == null || loginRequestDTO.getPassword() == null) {
            log.error("AuthenticationServiceImpl::loginUser LoginRequestDTO is empty");
            throw new UserNotFoundException("Login User Request is empty");
        }

        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email " + loginRequestDTO.getEmail()));

        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));

            if (!authenticate.isAuthenticated()) {
                log.error("AuthenticationServiceImpl::loginUser User is not authenticated");
                throw new BadCredentialsException("User is not authenticated");
            }

            SecurityContextHolder.getContext().setAuthentication(authenticate);
            log.info("AuthenticationServiceImpl::loginUser User is authenticated");

            String generatedToken = jwtTokenUtil.generateToken(user.getEmail(), user.getRole().name());

            return LoginResponseDTO.builder()
                    .accessToken(generatedToken)
                    .build();
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            throw new BadCredentialsException("User is not authenticated");
        }
    }

    @Override
    public String updatePassword(UpdatePasswordRequestDto updatePasswordRequestDto) {
        log.info("AuthenticationServiceImpl::updatePassword {}", updatePasswordRequestDto);

        if (!(StringUtils.hasText(updatePasswordRequestDto.getOldPassword())
                && StringUtils.hasText(updatePasswordRequestDto.getNewPassword())
                && StringUtils.hasText(updatePasswordRequestDto.getConfirmPassword()))) {
            log.error("AuthenticationServiceImpl::updatePassword UpdatePasswordRequestDto is empty");
            throw new UserNotFoundException("Update Password Request is empty");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            throw new IllegalStateException("User is not authenticated");
        }

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found with email " + authentication.getName()));

        if (!passwordEncoder.matches(updatePasswordRequestDto.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Old password is incorrect");
        }

        if (!updatePasswordRequestDto.getNewPassword().equals(updatePasswordRequestDto.getConfirmPassword())) {
            throw new BadCredentialsException("New password and confirm password do not match");
        }


        user.setPassword(passwordEncoder.encode(updatePasswordRequestDto.getNewPassword()));
        User updatedUser = userRepository.save(user);
        log.info("AuthenticationServiceImpl::updatePassword updated user {}", updatedUser);

        return "Password updated successfully";
    }
}
