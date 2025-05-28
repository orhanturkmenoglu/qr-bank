package com.example.qr_bank.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login request data")
public class LoginRequestDTO {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    @Schema(description = "User's email for login", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Schema(description = "User's password", example = "P@ssw0rd!")
    private String password;
}
