package com.example.qr_bank.service.impl;

import com.example.qr_bank.dto.request.AccountUpdateRequestDTO;
import com.example.qr_bank.dto.response.AccountResponseDTO;
import com.example.qr_bank.exception.AccountIbanNotFoundException;
import com.example.qr_bank.exception.AccountNotFoundException;
import com.example.qr_bank.mapper.AccountMapper;
import com.example.qr_bank.model.Account;
import com.example.qr_bank.repository.AccountRepository;
import com.example.qr_bank.service.AccountService;
import com.example.qr_bank.utils.AESEncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final AESEncryptionUtil encryptionUtil;


    @Override
    public AccountResponseDTO getAccountById(String id) {
        log.info("AccountServiceImpl::getAccountById {}", id);

        Account account = accountRepository.findById(id).orElseThrow(
                () -> new AccountNotFoundException("Account not found")
        );

        return accountMapper.toAccountResponseDTO(account);
    }

    @Override
    public List<AccountResponseDTO> getAllAccountsByUserId(String userId) {
        log.info("AccountServiceImpl::getAllAccountsByUserId {}", userId);

        List<Account> accountList = accountRepository.findAllByOwnerId(userId);

        if (accountList.isEmpty()) {
            log.error("AccountServiceImpl::getAllAccountsByUserId Account list is empty");
            throw new AccountNotFoundException("Account not found");
        }

        return accountMapper.toAccountResponseDTOs(accountList);
    }

    @Override
    public AccountResponseDTO updateAccount(String id, AccountUpdateRequestDTO accountUpdateRequestDTO) {
        log.info("AccountServiceImpl::updateAccount {}", accountUpdateRequestDTO);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setCurrency(accountUpdateRequestDTO.getCurrencyType());
        account.setBalance(accountUpdateRequestDTO.getBalance());

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

    @Override
    public boolean existsAccountByIban(String senderAccountIban) {
        log.info("AccountServiceImpl::findAccountByIban {}", senderAccountIban);

        boolean existsAccountByIban = accountRepository.existsAccountByIban(senderAccountIban);

        if (!existsAccountByIban) {
            throw new AccountIbanNotFoundException("User account iban not found");
        }

        return true;
    }

    @Override
    public AccountResponseDTO getAccountByIban(String iban) {
        log.info("AccountServiceImpl::getAccountyByIban {}", iban);

        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new AccountIbanNotFoundException("User account iban not found"));

        return accountMapper.toAccountResponseDTO(account);
    }


}
