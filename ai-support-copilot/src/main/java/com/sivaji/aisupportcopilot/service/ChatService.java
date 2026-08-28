package com.sivaji.aisupportcopilot.service;

import com.sivaji.aisupportcopilot.dto.ChatRequest;
import com.sivaji.aisupportcopilot.dto.ChatResponse;

import java.util.UUID;

public interface ChatService {
    ChatResponse chat(
            UUID userId,
            ChatRequest request
    );
}
