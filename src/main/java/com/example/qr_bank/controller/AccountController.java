package com.example.qr_bank.controller;

import com.example.qr_bank.dto.request.AccountRequestDTO;
import com.example.qr_bank.dto.request.AccountUpdateRequestDTO;
import com.example.qr_bank.dto.response.AccountResponseDTO;
import com.example.qr_bank.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "Account management API")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Get account by ID", description = "Retrieves account information by account ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccountById(
            @Parameter(description = "ID of the account to retrieve", required = true)
            @PathVariable String id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @Operation(summary = "Get accounts by user ID", description = "Retrieves all accounts associated with a specific user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User or accounts not found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountResponseDTO>> getAccountsByUserId(
            @Parameter(description = "ID of the user whose accounts are to be retrieved", required = true)
            @PathVariable String userId) {
        return ResponseEntity.ok(accountService.getAllAccountsByUserId(userId));
    }

    @Operation(summary = "Update account", description = "Updates an existing account by ID with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> updateAccount(
            @Parameter(description = "ID of the account to update", required = true)
            @PathVariable String id,
            @Parameter(description = "Updated account details", required = true)
            @Valid @RequestBody AccountUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(accountService.updateAccount(id, requestDTO));
    }

    @Operation(summary = "Delete account", description = "Deletes an account by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @Parameter(description = "ID of the account to delete", required = true)
            @PathVariable String id) {
        accountService.deleteAccountById(id);
        return ResponseEntity.noContent().build();
    }
}
