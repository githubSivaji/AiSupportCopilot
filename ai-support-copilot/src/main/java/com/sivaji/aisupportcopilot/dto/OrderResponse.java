package com.sivaji.aisupportcopilot.dto;


import com.sivaji.aisupportcopilot.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(

        UUID id,

        UUID userId,

        OrderStatus status,

        BigDecimal totalAmount,

        LocalDateTime createdAt,

        List<OrderItemResponse> items
) {
}