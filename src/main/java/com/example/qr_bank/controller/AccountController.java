package com.example.qr_bank.controller;

import com.example.qr_bank.dto.request.AccountRequestDTO;
import com.example.qr_bank.dto.request.AccountUpdateRequestDTO;
import com.example.qr_bank.dto.response.AccountResponseDTO;
import com.example.qr_bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccountById(@PathVariable String id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountResponseDTO>> getAccountsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(accountService.getAllAccountsByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> updateAccount(
            @PathVariable String id,
            @RequestBody AccountUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(accountService.updateAccount(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        accountService.deleteAccountById(id);
        return ResponseEntity.noContent().build();
    }
}
