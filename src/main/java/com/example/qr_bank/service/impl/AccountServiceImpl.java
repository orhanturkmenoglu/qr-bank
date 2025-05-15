package com.example.qr_bank.service.impl;

import com.example.qr_bank.dto.request.AccountRequestDTO;
import com.example.qr_bank.dto.response.AccountResponseDTO;
import com.example.qr_bank.exception.UserNotFoundException;
import com.example.qr_bank.mapper.AccountMapper;
import com.example.qr_bank.model.Account;
import com.example.qr_bank.model.User;
import com.example.qr_bank.repository.AccountRepository;
import com.example.qr_bank.repository.UserRepository;
import com.example.qr_bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Override
    public AccountResponseDTO createAccount(String userId, AccountRequestDTO accountRequestDTO) {
        log.info("AccountServiceImpl::createAccount {}", accountRequestDTO);

        if (!StringUtils.hasText(userId)) {
            log.error("AccountServiceImpl::createAccount User id is null");
            throw new UserNotFoundException("User id is null");
        }

        if (Objects.isNull(accountRequestDTO)) {
            log.error("AccountServiceImpl::createAccount AccountRequestDTO is null");
            throw new NullPointerException("AccountRequestDTO is null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Account account = accountMapper.toAccount(accountRequestDTO);
        account.setId(UUID.randomUUID().toString());
        account.setOwner(user);
        account.setIban(generateIban());

        Account savedAccount = accountRepository.save(account);
        log.info("AccountServiceImpl::createAccount saved account {}", savedAccount);

        return accountMapper.toAccountResponseDTO(savedAccount);
    }


    @Override
    public AccountResponseDTO getAccountById(String id) {
        return null;
    }

    @Override
    public List<AccountResponseDTO> getAllAccountsByUserId(String userId) {
        log.info("AccountServiceImpl::getAllAccountsByUserId {}", userId);

        Account account = accountRepository.findAllByOwnerId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return accountMapper.toAccountResponseDTOs(account);
    }

    @Override
    public AccountResponseDTO updateAccount(String id, AccountRequestDTO accountRequestDTO) {
        log.info("AccountServiceImpl::updateAccount {}", accountRequestDTO);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setCurrency(accountRequestDTO.getCurrencyType());

        Account updatedAccount = accountRepository.save(account);
        log.info("AccountServiceImpl::updateAccount updated account {}", updatedAccount);

        return accountMapper.toAccountResponseDTO(updatedAccount);
    }

    @Override
    public void deleteAccountById(String id) {
        log.info("AccountServiceImpl::deleteAccountById {}", id);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        accountRepository.deleteById(account.getId());

        log.info("AccountServiceImpl::deleteAccountById deleted account {}", account);
    }

    private String generateIban() {
        return "TR" + UUID.randomUUID().toString().replace("-", "").substring(0, 24);
    }

}
