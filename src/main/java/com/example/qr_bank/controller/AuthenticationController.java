package com.example.qr_bank.controller;

import com.example.qr_bank.dto.request.LoginRequestDTO;
import com.example.qr_bank.dto.request.UserRequestDTO;
import com.example.qr_bank.dto.response.LoginResponseDTO;
import com.example.qr_bank.dto.response.UserResponseDTO;
import com.example.qr_bank.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Register user", description = "Registers a new user with the provided user details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<UserResponseDTO> registerUser(
            @Parameter(description = "User details to create", required = true)
            @Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO createdUser = authenticationService.registerUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(summary = "Login user", description = "Logs in a user with the provided login credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO = authenticationService.loginUser(loginRequestDTO);
        return ResponseEntity.ok(loginResponseDTO);
    }
}
