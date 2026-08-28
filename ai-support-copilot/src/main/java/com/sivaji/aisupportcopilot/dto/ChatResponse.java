package com.sivaji.aisupportcopilot.dto;

import java.util.UUID;

public record ChatResponse(

        UUID conversationId,

        String response
) {
}