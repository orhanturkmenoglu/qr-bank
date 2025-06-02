package com.example.qr_bank.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Request data for creating a new user")
public class UserRequestDTO implements Serializable {

    @Schema(description = "The user's identity number", example = "123456789")
    public String identityNumber;

    @Schema(description = "The user's first name", example = "John")
    private String firstName;

    @Schema(description = "The user's last name", example = "Doe")
    private String lastName;

    @Schema(description = "The user's email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "The user's password", example = "secureP@ssword123")
    private String password;

    @Schema(description = "The user's phone number", example = "555-1234")
    private String telephoneNumber;

    @Schema(description = "List of accounts to create for the user")
    private List<AccountRequestDTO> accountRequestDTOS;


}
