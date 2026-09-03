package com.sivaji.aisupportcopilot.ai.orchestrator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivaji.aisupportcopilot.ai.LlmGateway.LlmGateway;
import com.sivaji.aisupportcopilot.ai.rag.RagService;
import com.sivaji.aisupportcopilot.ai.tool.SupportToolService;
import com.sivaji.aisupportcopilot.dto.OrderToolResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrchestratorService {

    private final RagService ragService;
    private final LlmGateway llmGateway;
    private final ObjectMapper objectMapper;
    private final SupportToolService supportToolService;

    public String process(
            String message,
            UUID userId) {

        RouteDecision decision = determineRoute(message);

        return switch (decision.route()) {

            case "RAG" ->
                    ragService.answer(message);

            case "ORDER" -> {

                if (decision.orderId() == null) {
                    yield "Please provide your order ID.";
                }

                UUID orderId;

                try {
                    orderId = UUID.fromString(decision.orderId());
                } catch (IllegalArgumentException e) {
                    yield "Please provide a valid order ID.";
                }

                OrderToolResponse result =
                        supportToolService.getOrderStatus(
                                orderId,
                                userId
                        );

                yield formatOrderResponse(result);
            }

            case "TICKET" ->
                    supportToolService.createSupportTicket(
                            message,
                            userId
                    );

            case "CHAT" ->
                    llmGateway.chat(
                            "You are a helpful customer support assistant.",
                            message
                    );

            default ->
                    "I couldn't determine how to handle your request.";
        };
    }

    private String formatOrderResponse(OrderToolResponse result) {

        if ("NOT_FOUND".equals(result.status())) {
            return "Order not found.";
        }

        if ("UNAUTHORIZED".equals(result.status())) {
            return "You are not authorized to access this order.";
        }

        return "Order "
                + result.orderId()
                + " is currently "
                + result.status()
                + ". Total amount: "
                + result.totalAmount();
    }

    private RouteDecision determineRoute(String message) {

        String routingPrompt = """
                Classify the customer request.

                Valid routes:
                - RAG
                - ORDER
                - TICKET
                - CHAT

                Rules:

                RAG:
                Questions about company policies,
                shipping, returns, FAQs and documentation.

                ORDER:
                Questions about a specific order.
                Extract the order ID if present.

                TICKET:
                Customer wants to create, raise, or open
                a support ticket.

                CHAT:
                Greetings, casual conversation, or general chat.

                Return ONLY valid JSON.

                Format:
                {
                  "route": "RAG|ORDER|TICKET|CHAT",
                  "orderId": "UUID or null"
                }

                Customer message:
                %s
                """.formatted(message);

        String response = llmGateway.chat(
                "You are a strict JSON request classifier.",
                routingPrompt
        );

        try {

            return objectMapper.readValue(
                    response,
                    RouteDecision.class
            );

        } catch (JsonProcessingException e) {

            throw new IllegalStateException(
                    "Failed to parse routing decision from LLM response: "
                            + response,
                    e
            );
        }
    }
}