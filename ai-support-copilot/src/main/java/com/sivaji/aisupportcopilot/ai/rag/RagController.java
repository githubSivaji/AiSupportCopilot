package com.sivaji.aisupportcopilot.ai.rag;


import com.sivaji.aisupportcopilot.ai.rag.RagService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rag")
@RequiredArgsConstructor
public class RagController {

    private final RagService ragService;

    @GetMapping("/ask")
    public ResponseEntity<String> ask(
            @RequestParam String question) {

        return ResponseEntity.ok(
                ragService.answer(question)
        );
    }
}