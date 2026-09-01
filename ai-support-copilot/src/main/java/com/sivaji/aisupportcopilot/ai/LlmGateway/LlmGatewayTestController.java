package com.sivaji.aisupportcopilot.ai.LlmGateway;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/gateway")
@RequiredArgsConstructor
public class LlmGatewayTestController {

    private final LlmGateway llmGateway;

    @GetMapping("/test")
    public ResponseEntity<String> test(
            @RequestParam String message) {

        String response = llmGateway.chat(
                "You are a helpful customer support assistant.",
                message
        );

        return ResponseEntity.ok(response);
    }
}