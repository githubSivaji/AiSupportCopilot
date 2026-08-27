package com.sivaji.aisupportcopilot.dto;


import com.sivaji.aisupportcopilot.enums.TicketStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicketResponse(

        UUID id,

        UUID userId,

        String subject,

        String description,

        TicketStatus status,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}