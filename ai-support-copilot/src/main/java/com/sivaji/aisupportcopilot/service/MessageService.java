package com.sivaji.aisupportcopilot.service;

import com.sivaji.aisupportcopilot.entity.Message;
import com.sivaji.aisupportcopilot.enums.MessageRole;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    Message addMessage(
            UUID userId,
            UUID conversationId,
            MessageRole role,
            String content
    );

    List<Message> getConversationMessages(
            UUID userId,
            UUID conversationId
    );
}