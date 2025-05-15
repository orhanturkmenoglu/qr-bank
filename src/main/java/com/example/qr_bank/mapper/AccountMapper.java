package com.example.qr_bank.mapper;

import com.example.qr_bank.dto.request.AccountRequestDTO;
import com.example.qr_bank.dto.response.AccountResponseDTO;
import com.example.qr_bank.model.Account;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account toAccount(AccountRequestDTO accountRequestDTO);

    AccountResponseDTO  toAccountResponseDTO(Account account);

    List<AccountResponseDTO> toAccountResponseDTOs(List<Account> accounts);
}
