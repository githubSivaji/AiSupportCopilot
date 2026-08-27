package com.sivaji.aisupportcopilot.service;

import com.sivaji.aisupportcopilot.dto.TicketCreateRequest;
import com.sivaji.aisupportcopilot.dto.TicketResponse;
import com.sivaji.aisupportcopilot.entity.SupportTicket;

import java.util.List;
import java.util.UUID;

public interface SupportTicketService {

    TicketResponse createTicket(
            UUID userId,
            TicketCreateRequest request
    );

    TicketResponse getTicket(
            UUID userId,
            UUID ticketId
    );

    List<TicketResponse> getUserTickets(
            UUID userId
    );

    TicketResponse updateStatus(
            UUID ticketId,
            String status
    );
}