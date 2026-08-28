package com.sivaji.aisupportcopilot.service;
import com.sivaji.aisupportcopilot.ai.tool.OrderToolService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LlmServiceImpl implements LlmService {

    private final ChatClient chatClient;
    private final OrderToolService orderToolService;

    @Override
    public String generateResponse(String prompt, UUID userId) {


        Map<String, Object> toolContext =
                Map.of("userId", userId);
        return chatClient
                .prompt()
                .user(prompt)
                .tools(orderToolService)
                .toolContext(toolContext)
                .call()
                .content();
    }
}