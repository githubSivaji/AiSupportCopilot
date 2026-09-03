package com.sivaji.aisupportcopilot.ai.agent;

import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SupportAgentImpl implements SupportAgent {

    private final SupportAgentApi agent;
    public SupportAgentImpl(
            SupportAgentTools tools) {

        this.agent = AiServices.builder(SupportAgentApi.class)
                .tools(tools)
                .build();
    }

    @Override
    public String chat(String message,  UUID userId) {
        return agent.chat(message);
    }
    interface SupportAgentApi {

        String chat(
                @UserMessage String message
        );
    }
}
