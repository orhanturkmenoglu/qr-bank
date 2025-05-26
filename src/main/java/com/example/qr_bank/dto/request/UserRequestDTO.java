package com.example.qr_bank.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO implements Serializable {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private List<AccountRequestDTO> accountRequestDTOS;
}
