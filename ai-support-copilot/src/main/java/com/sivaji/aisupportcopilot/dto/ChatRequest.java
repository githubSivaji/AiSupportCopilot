package com.sivaji.aisupportcopilot.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ChatRequest(

        UUID conversationId,

        @NotBlank(message = "Message is required")
        String message
) {
}
