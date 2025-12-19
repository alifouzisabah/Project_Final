package com.example.Project_Finall.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordDTO(
        String username,
        @NotBlank(message = "New Password is required")
        @Size(min = 3, max = 9, message = "NewPassword must be between 3 and 9 characters")
        String newPassword,
        @NotBlank(message = "Repeat NEW password is required")
        String repeatNewPassword
){
}
