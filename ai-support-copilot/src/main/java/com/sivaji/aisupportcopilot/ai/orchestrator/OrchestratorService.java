package com.sivaji.aisupportcopilot.ai.orchestrator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivaji.aisupportcopilot.ai.LlmGateway.LlmGateway;
import com.sivaji.aisupportcopilot.ai.rag.RagService;
import com.sivaji.aisupportcopilot.ai.tool.OrderToolService;
import com.sivaji.aisupportcopilot.ai.tool.SupportTicketToolService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrchestratorService {

    private final RagService ragService;
    private final LlmGateway llmGateway;
    private final ObjectMapper objectMapper;
    private final OrderToolService orderToolService;
    private final SupportTicketToolService supportTicketToolService;

    public String process(
            String message,
            UUID userId) {

        RouteDecision decision =
                determineRoute(message);

        return switch (decision.route()) {

            case "RAG" ->
                    ragService.answer(message);

            case "ORDER" -> {

                if (decision.orderId() == null) {
                    yield "Please provide your order ID.";
                }

                yield orderToolService.getOrderStatus(
                        UUID.fromString(decision.orderId()),
                        userId
                );
            }

            case "TICKET" ->
                    supportTicketToolService.createTicket(
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

        return objectMapper.readValue(
                response,
                RouteDecision.class
        );
    }
}