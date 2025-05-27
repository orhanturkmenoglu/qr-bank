package com.example.qr_bank.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User response details")
public class UserResponseDTO implements Serializable {
    @Schema(description = "Unique identifier of the user", example = "user-1234")
    private String id;

    @JsonProperty("first_name")
    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @JsonProperty("last_name")
    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "User creation timestamp", example = "2025-05-27T14:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "User last update timestamp", example = "2025-05-27T15:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "List of user's accounts")
    private List<AccountResponseDTO> accountList;
}
