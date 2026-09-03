package com.sivaji.aisupportcopilot.ai.agent;

import com.sivaji.aisupportcopilot.ai.LlmGateway.LlmGateway;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;

import java.util.List;

public class GatewayChatModel implements ChatModel {
    private final LlmGateway llmGateway;

    public GatewayChatModel(LlmGateway llmGateway) {
        this.llmGateway = llmGateway;
    }

    @Override
    public ChatResponse chat(List<ChatMessage> messages) {

        String systemPrompt = "";
        String userPrompt = "";

        for (ChatMessage message : messages) {

            switch (message.type()) {

                case SYSTEM -> systemPrompt = message.text();

                case USER -> userPrompt = message.text();

                default -> {
                    // Ignore other message types for now
                }
            }
        }

        String response =
                llmGateway.chat(
                        systemPrompt,
                        userPrompt
                );

        return ChatResponse.builder()
                .aiMessage(AiMessage.from(response))
                .metadata(ChatResponseMetadata.builder().build())
                .build();
    }

}
