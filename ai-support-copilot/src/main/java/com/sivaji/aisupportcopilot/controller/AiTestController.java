package com.sivaji.aisupportcopilot.controller;
import com.sivaji.aisupportcopilot.entity.User;
import com.sivaji.aisupportcopilot.service.LlmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiTestController {

    private final LlmService llmService;

    @GetMapping("/test")
    public ResponseEntity<String> test( @AuthenticationPrincipal User user,
            @RequestParam String message) {



        String response =
                llmService.generateResponse(message,  user.getId());

        return ResponseEntity.ok(response);
    }
}