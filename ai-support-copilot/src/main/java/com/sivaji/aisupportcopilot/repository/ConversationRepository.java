package com.sivaji.aisupportcopilot.repository;
import com.sivaji.aisupportcopilot.entity.Conversation;
import com.sivaji.aisupportcopilot.enums.ConversationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ConversationRepository
        extends JpaRepository<Conversation, UUID> {

    List<Conversation> findByUserId(UUID userId);

    List<Conversation> findByUserIdAndStatus(
            UUID userId,
            ConversationStatus status
    );
}
