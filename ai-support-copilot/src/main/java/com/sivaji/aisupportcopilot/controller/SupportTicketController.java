package com.sivaji.aisupportcopilot.controller;
import com.sivaji.aisupportcopilot.entity.User;
import com.sivaji.aisupportcopilot.dto.TicketCreateRequest;
import com.sivaji.aisupportcopilot.dto.TicketResponse;
import com.sivaji.aisupportcopilot.entity.User;
import com.sivaji.aisupportcopilot.service.SupportTicketService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class SupportTicketController {

    private final SupportTicketService supportTicketService;

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody TicketCreateRequest request) {

        TicketResponse response =
                supportTicketService.createTicket(
                        user.getId(),
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> getMyTickets(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                supportTicketService.getUserTickets(
                        user.getId()
                )
        );
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> getTicket(
            @AuthenticationPrincipal User user,
            @PathVariable UUID ticketId) {

        return ResponseEntity.ok(
                supportTicketService.getTicket(
                        user.getId(),
                        ticketId
                )
        );
    }

    @PatchMapping("/{ticketId}/status")
    public ResponseEntity<TicketResponse> updateStatus(
            @PathVariable UUID ticketId,
            @RequestParam String status) {

        return ResponseEntity.ok(
                supportTicketService.updateStatus(
                        ticketId,
                        status
                )
        );
    }
}
