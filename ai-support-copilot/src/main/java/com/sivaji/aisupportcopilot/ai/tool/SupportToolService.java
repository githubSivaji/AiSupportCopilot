package com.sivaji.aisupportcopilot.ai.tool;

import com.sivaji.aisupportcopilot.dto.OrderToolResponse;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SupportToolService {

    private final OrderToolService orderToolService;
    private final SupportTicketToolService supportTicketToolService;

    public SupportToolService(
            OrderToolService orderToolService,
            SupportTicketToolService supportTicketToolService) {

        this.orderToolService = orderToolService;
        this.supportTicketToolService = supportTicketToolService;
    }

    public OrderToolResponse getOrderStatus(
            UUID orderId,
            UUID userId) {

        ToolContext toolContext = new ToolContext(
                java.util.Map.of("userId", userId)
        );

        return orderToolService.getOrderStatus(
                orderId,
                toolContext
        );
    }

    public String createSupportTicket(
            String issue,
            UUID userId) {

        ToolContext toolContext = new ToolContext(
                java.util.Map.of("userId", userId)
        );

        return supportTicketToolService.createSupportTicket(
                issue,
                toolContext
        );
    }
}