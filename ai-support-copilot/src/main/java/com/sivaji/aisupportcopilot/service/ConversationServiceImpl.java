package com.sivaji.aisupportcopilot.service;

import com.sivaji.aisupportcopilot.entity.Conversation;
import com.sivaji.aisupportcopilot.entity.User;
import com.sivaji.aisupportcopilot.enums.ConversationStatus;
import com.sivaji.aisupportcopilot.repository.ConversationRepository;
import com.sivaji.aisupportcopilot.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    @Override
    public Conversation createConversation(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found: " + userId
                        )
                );

        Conversation conversation = Conversation.builder()
                .user(user)
                .status(ConversationStatus.ACTIVE)
                .build();

        return conversationRepository.save(conversation);
    }

    @Override
    @Transactional(readOnly = true)
    public Conversation getConversation(
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

        validateOwnership(conversation, userId);

        return conversation;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Conversation> getUserConversations(
            UUID userId) {

        return conversationRepository
                .findByUserId(userId);
    }

    @Override
    public void closeConversation(
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

        validateOwnership(conversation, userId);

        if (conversation.getStatus()
                == ConversationStatus.CLOSED) {

            throw new RuntimeException(
                    "Conversation is already closed"
            );
        }

        conversation.setStatus(
                ConversationStatus.CLOSED
        );
    }

    private void validateOwnership(
            Conversation conversation,
            UUID userId) {

        if (!conversation.getUser().getId().equals(userId)) {
            throw new RuntimeException(
                    "You cannot access this conversation"
            );
        }
    }
}
