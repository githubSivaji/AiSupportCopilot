package com.sivaji.aisupportcopilot.service;


import com.sivaji.aisupportcopilot.dto.ChatRequest;
import com.sivaji.aisupportcopilot.dto.ChatResponse;
import com.sivaji.aisupportcopilot.entity.Conversation;
import com.sivaji.aisupportcopilot.entity.Message;
import com.sivaji.aisupportcopilot.enums.ConversationStatus;
import com.sivaji.aisupportcopilot.enums.MessageRole;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ConversationService conversationService;
    private final MessageService messageService;
    private final LlmService llmService;

    @Override
    @Transactional
    public ChatResponse chat(
            UUID userId,
            ChatRequest request) {

        Conversation conversation;

        /*
         * Create a new conversation if the client
         * didn't provide a conversationId.
         */
        if (request.conversationId() == null) {

            conversation =
                    conversationService
                            .createConversation(userId);

        } else {

            conversation =
                    conversationService.getConversation(
                            userId,
                            request.conversationId()
                    );

            if (conversation.getStatus()
                    == ConversationStatus.CLOSED) {

                throw new RuntimeException(
                        "Conversation is already closed"
                );
            }
        }

        UUID conversationId = conversation.getId();

        /*
         * 1. Save user's message
         */
        messageService.addMessage(
                userId,
                conversationId,
                MessageRole.USER,
                request.message()
        );

        /*
         * 2. Load conversation history
         */
        List<Message> messages =
                messageService.getConversationMessages(
                        userId,
                        conversationId
                );

        /*
         * 3. Build prompt for Gemini
         */
        StringBuilder prompt = new StringBuilder();

        prompt.append("""
                You are an AI customer support assistant.
                Be helpful, concise, and professional.

                Conversation history:

                """);

        for (Message message : messages) {

            prompt.append(
                    message.getRole()
            );

            prompt.append(": ");

            prompt.append(
                    message.getContent()
            );

            prompt.append("\n");
        }

        /*
         * 4. Ask Gemini
         */
        String aiResponse =
                llmService.generateResponse(
                        prompt.toString(),userId
                );

        /*
         * 5. Save AI response
         */
        messageService.addMessage(
                userId,
                conversationId,
                MessageRole.ASSISTANT,
                aiResponse
        );

        /*
         * 6. Return response
         */
        return new ChatResponse(
                conversationId,
                aiResponse
        );
    }
}
