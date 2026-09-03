package com.sivaji.aisupportcopilot.ai.agent;

import com.sivaji.aisupportcopilot.ai.rag.RagService;
import com.sivaji.aisupportcopilot.ai.tool.SupportToolService;
import com.sivaji.aisupportcopilot.dto.OrderToolResponse;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SupportAgentTools {

    private final RagService ragService;
    private final SupportToolService supportToolService;

    public SupportAgentTools(
            RagService ragService,
            SupportToolService supportToolService) {

        this.ragService = ragService;
        this.supportToolService = supportToolService;
    }

    @Tool("""
            Search the company knowledge base.
            Use this for questions about return policies,
            shipping policies, FAQs, documentation and other
            company-specific information.
            """)
    public String searchKnowledgeBase(String question) {

        return ragService.answer(question);
    }

    @Tool("""
            Get the status and total amount of an order.
            Use this when the customer asks about a specific order.
            """)
    public String getOrderStatus(String orderId) {

        return "ORDER_TOOL_PLACEHOLDER:" + orderId;
    }

    @Tool("""
            Create a support ticket.
            Use this when the customer wants to raise,
            create or open a support ticket.
            """)
    public String createSupportTicket(String issue) {

        return "TICKET_TOOL_PLACEHOLDER:" + issue;
    }

}
