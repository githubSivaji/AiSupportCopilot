package com.sivaji.aisupportcopilot.service;

import com.sivaji.aisupportcopilot.entity.Conversation;
import com.sivaji.aisupportcopilot.entity.Message;

import com.sivaji.aisupportcopilot.enums.ConversationStatus;
import com.sivaji.aisupportcopilot.enums.MessageRole;
import com.sivaji.aisupportcopilot.repository.ConversationRepository;
import com.sivaji.aisupportcopilot.repository.MessageRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    @Override
    public Message addMessage(
            UUID userId,
            UUID conversationId,
            MessageRole role,
            String content) {

        Conversation conversation =
                conversationRepository.findById(conversationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Conversation not found: "
                                                + conversationId
                                )
                        );

        if (!conversation.getUser().getId().equals(userId)) {
            throw new RuntimeException(
                    "You cannot access this conversation"
            );
        }

        if (conversation.getStatus()
                == ConversationStatus.CLOSED) {

            throw new RuntimeException(
                    "Conversation is already closed"
            );
        }

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException(
                    "Message content cannot be empty"
            );
        }

        Message message = Message.builder()
                .conversation(conversation)
                .role(role)
                .content(content)
                .build();

        return messageRepository.save(message);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> getConversationMessages(
            UUID userId,
            UUID conversationId) {

        Conversation conversation =
                conversationRepository.findById(conversationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Conversation not found: "
                                                + conversationId
                                )
                        );

        if (!conversation.getUser().getId().equals(userId)) {
            throw new RuntimeException(
                    "You cannot access this conversation"
            );
        }

        return messageRepository
                .findByConversationIdOrderByCreatedAtAsc(
                        conversationId
                );
    }
}