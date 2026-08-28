package com.sivaji.aisupportcopilot.ai.tool;

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
            name = "getOrderStatus",
            description = "Get the status of an order belonging to the authenticated customer"
    )
    public String getOrderStatus(
            String orderId, ToolContext toolContext) {

        UUID orderUUID;
        try {
            orderUUID = UUID.fromString(orderId);
        } catch (IllegalArgumentException e) {
            return "Invalid order ID format. Please provide a valid order ID.";
        }

        UUID userId = (UUID) toolContext
                .getContext()
                .get("userId");

        Order order = orderRepository.findById(orderUUID)
                .orElse(null);

        if (order == null) {
            return "Order not found.";
        }

        // Security: customer can only access own order
        if (!order.getUser().getId().equals(userId)) {
            return "You are not authorized to access this order.";
        }

        return "Order status: " + order.getStatus();
    }
}
