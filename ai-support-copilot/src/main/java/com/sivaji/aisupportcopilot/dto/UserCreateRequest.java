package com.sivaji.aisupportcopilot.dto;

import com.sivaji.aisupportcopilot.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateRequest (
    @NotBlank
    @Email
    String email,
    @NotBlank
    @Size(min = 8, max = 100)
    String password,

    @NotNull
    Role role
)
{
    
}