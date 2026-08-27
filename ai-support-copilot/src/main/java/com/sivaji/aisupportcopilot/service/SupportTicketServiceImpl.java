package com.sivaji.aisupportcopilot.service;

import com.sivaji.aisupportcopilot.dto.TicketCreateRequest;
import com.sivaji.aisupportcopilot.dto.TicketResponse;
import com.sivaji.aisupportcopilot.entity.SupportTicket;
import com.sivaji.aisupportcopilot.entity.User;
import com.sivaji.aisupportcopilot.enums.TicketStatus;
import com.sivaji.aisupportcopilot.repository.SupportTicketRepository;
import com.sivaji.aisupportcopilot.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SupportTicketServiceImpl implements SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;
    private final UserRepository userRepository;

    @Override
    public TicketResponse createTicket(
            UUID userId,
            TicketCreateRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found: " + userId
                        )
                );

        SupportTicket ticket = SupportTicket.builder()
                .user(user)
                .subject(request.subject())
                .description(request.description())
                .status(TicketStatus.OPEN)
                .build();

        SupportTicket savedTicket =
                supportTicketRepository.save(ticket);

        return toResponse(savedTicket);
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponse getTicket(
            UUID userId,
            UUID ticketId) {

        SupportTicket ticket =
                supportTicketRepository.findById(ticketId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Ticket not found: "
                                                + ticketId
                                )
                        );

        validateOwnership(ticket, userId);

        return toResponse(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponse> getUserTickets(
            UUID userId) {

        return supportTicketRepository
                .findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public TicketResponse updateStatus(
            UUID ticketId,
            String status) {

        SupportTicket ticket =
                supportTicketRepository.findById(ticketId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Ticket not found: "
                                                + ticketId
                                )
                        );

        TicketStatus newStatus;

        try {
            newStatus = TicketStatus.valueOf(
                    status.toUpperCase()
            );
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(
                    "Invalid ticket status: " + status
            );
        }

        ticket.setStatus(newStatus);

        SupportTicket updatedTicket =
                supportTicketRepository.save(ticket);

        return toResponse(updatedTicket);
    }

    private void validateOwnership(
            SupportTicket ticket,
            UUID userId) {

        if (!ticket.getUser().getId().equals(userId)) {
            throw new RuntimeException(
                    "You cannot access this ticket"
            );
        }
    }

    private TicketResponse toResponse(
            SupportTicket ticket) {

        return new TicketResponse(
                ticket.getId(),
                ticket.getUser().getId(),
                ticket.getSubject(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }
}