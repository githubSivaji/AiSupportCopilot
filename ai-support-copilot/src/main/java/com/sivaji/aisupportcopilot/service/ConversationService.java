package com.sivaji.aisupportcopilot.service;

import com.sivaji.aisupportcopilot.entity.Conversation;

import java.util.List;
import java.util.UUID;

public interface ConversationService {

    Conversation createConversation(UUID userId);

    Conversation getConversation(
            UUID userId,
            UUID conversationId
    );

    List<Conversation> getUserConversations(UUID userId);

    void closeConversation(
            UUID userId,
            UUID conversationId
    );
}
