package com.sivaji.aisupportcopilot.controller;
import com.sivaji.aisupportcopilot.entity.Message;
import com.sivaji.aisupportcopilot.entity.User;
import com.sivaji.aisupportcopilot.enums.MessageRole;
import com.sivaji.aisupportcopilot.service.MessageService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/conversations/{conversationId}/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<List<Message>> getMessages(
            @AuthenticationPrincipal User user,
            @PathVariable UUID conversationId) {

        return ResponseEntity.ok(
                messageService.getConversationMessages(
                        user.getId(),
                        conversationId
                )
        );
    }

    @PostMapping
    public ResponseEntity<Message> addMessage(
            @AuthenticationPrincipal User user,
            @PathVariable UUID conversationId,
            @RequestParam MessageRole role,
            @RequestParam String content) {

        Message message =
                messageService.addMessage(
                        user.getId(),
                        conversationId,
                        role,
                        content
                );

        return ResponseEntity.ok(message);
    }
}
