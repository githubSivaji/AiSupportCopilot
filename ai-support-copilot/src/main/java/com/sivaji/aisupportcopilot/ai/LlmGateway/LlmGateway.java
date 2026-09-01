package com.sivaji.aisupportcopilot.ai.LlmGateway;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class LlmGateway {
    private final ChatClient chatClient;

    public LlmGateway(ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    @Cacheable(
            value = "llmResponses",
            key = "#systemPrompt + '|' + #userPrompt"
    )
    @CircuitBreaker(
            name = "gemini",
            fallbackMethod = "fallback"
    )
    public String chat(String systemPrompt, String userPrompt) {

        return chatClient
                .prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();
    }
    public String fallback(
            String systemPrompt,
            String userPrompt,
            Throwable throwable) {

        return "The AI service is temporarily unavailable. "
                + "Please try again later.";
    }
}
