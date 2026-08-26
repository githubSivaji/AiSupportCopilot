package com.sivaji.aisupportcopilot.dto;

import com.sivaji.aisupportcopilot.enums.Role;

import java.util.UUID;

public record LoginResponse(
        UUID userId,
        String email,
        Role role,
        String token
) {
}
