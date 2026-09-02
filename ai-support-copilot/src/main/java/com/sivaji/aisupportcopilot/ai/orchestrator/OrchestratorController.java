package com.sivaji.aisupportcopilot.ai.orchestrator;


import com.sivaji.aisupportcopilot.ai.orchestrator.OrchestratorService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orchestrator")
@RequiredArgsConstructor
public class OrchestratorController {

    private final OrchestratorService orchestratorService;

    @GetMapping("/ask")
    public ResponseEntity<String> ask(
            @RequestParam String message) {

        return ResponseEntity.ok(
                orchestratorService.process(message)
        );
    }
}