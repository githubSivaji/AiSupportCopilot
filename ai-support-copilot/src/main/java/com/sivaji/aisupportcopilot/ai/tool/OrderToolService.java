package com.sivaji.aisupportcopilot.ai.tool;

import com.sivaji.aisupportcopilot.dto.OrderToolResponse;
import com.sivaji.aisupportcopilot.entity.Order;
import com.sivaji.aisupportcopilot.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderToolService {

    private final OrderRepository orderRepository;

    @Tool(
            description = "Get the status and total amount of an order belonging to the current user"
    )
    public OrderToolResponse getOrderStatus(
            UUID orderId,
            ToolContext toolContext) {

        UUID userId = (UUID) toolContext
                .getContext()
                .get("userId");

        Order order = orderRepository
                .findById(orderId)
                .orElse(null);

        if (order == null) {
            return new OrderToolResponse(
                    orderId,
                    "NOT_FOUND",
                    null
            );
        }

        if (!order.getUser().getId().equals(userId)) {
            return new OrderToolResponse(
                    orderId,
                    "UNAUTHORIZED",
                    null
            );
        }

        return new OrderToolResponse(
                order.getId(),
                order.getStatus().name(),
                order.getTotalAmount()
        );
    }
}
