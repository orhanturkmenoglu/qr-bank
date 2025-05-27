package com.example.qr_bank.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login request data")
public class LoginRequestDTO {

    @NotBlank(message = "Username must not be blank")
    @Schema(description = "Username or email for login", example = "john.doe@example.com")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Schema(description = "User's password", example = "P@ssw0rd!")
    private String password;
}
