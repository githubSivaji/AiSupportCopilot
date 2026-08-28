package com.sivaji.aisupportcopilot.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderToolResponse(
        UUID orderId,
        String status,
        BigDecimal totalAmount
) {
}