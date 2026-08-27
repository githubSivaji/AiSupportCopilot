package com.sivaji.aisupportcopilot.repository;

import com.sivaji.aisupportcopilot.entity.SupportTicket;

import com.sivaji.aisupportcopilot.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SupportTicketRepository
        extends JpaRepository<SupportTicket, UUID> {

    List<SupportTicket> findByUserId(UUID userId);

    List<SupportTicket> findByUserIdAndStatus(
            UUID userId,
            TicketStatus status
    );
}