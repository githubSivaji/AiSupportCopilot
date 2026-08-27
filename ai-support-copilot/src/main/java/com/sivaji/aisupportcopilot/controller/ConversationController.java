package com.sivaji.aisupportcopilot.controller;


import com.sivaji.aisupportcopilot.entity.Conversation;
import com.sivaji.aisupportcopilot.entity.User;
import com.sivaji.aisupportcopilot.service.ConversationService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping
    public ResponseEntity<Conversation> createConversation(
            @AuthenticationPrincipal User user) {

        Conversation conversation =
                conversationService.createConversation(
                        user.getId()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(conversation);
    }

    @GetMapping
    public ResponseEntity<List<Conversation>> getMyConversations(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                conversationService.getUserConversations(
                        user.getId()
                )
        );
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<Conversation> getConversation(
            @AuthenticationPrincipal User user,
            @PathVariable UUID conversationId) {

        return ResponseEntity.ok(
                conversationService.getConversation(
                        user.getId(),
                        conversationId
                )
        );
    }

    @PatchMapping("/{conversationId}/close")
    public ResponseEntity<Void> closeConversation(
            @AuthenticationPrincipal User user,
            @PathVariable UUID conversationId) {

        conversationService.closeConversation(
                user.getId(),
                conversationId
        );

        return ResponseEntity.noContent().build();
    }
}