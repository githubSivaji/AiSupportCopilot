package com.sivaji.aisupportcopilot.controller;

import com.sivaji.aisupportcopilot.dto.ChatRequest;
import com.sivaji.aisupportcopilot.dto.ChatResponse;
import com.sivaji.aisupportcopilot.entity.User;
import com.sivaji.aisupportcopilot.service.ChatService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ChatRequest request) {

        ChatResponse response =
                chatService.chat(
                        user.getId(),
                        request
                );

        return ResponseEntity.ok(response);
    }
}