package com.example.Project_Finall.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserDTO(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "Email must be a valid Gmail address")
        String email,

        @NotBlank(message = "Gender is required")
        String gender,

        @NotBlank(message = "Role is required")
        String role,

        @NotBlank(message = "Password is required")
        @Size(min = 3, max = 9, message = "Password must be between 3 and 9 characters")
        String password,
        @NotBlank(message = "Repeat password is required")
        String repeatPassword
) {
}
