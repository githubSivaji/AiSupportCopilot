package com.sivaji.aisupportcopilot.controller;

import com.sivaji.aisupportcopilot.service.DocumentIngestionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/documents")
@RequiredArgsConstructor
public class DocumentIngestionController {

    private final DocumentIngestionService documentIngestionService;

    @PostMapping
    @PreAuthorize("hasRole(\"ADMIN\")")
    public ResponseEntity<UUID> ingest(
            @Valid @RequestBody DocumentIngestionRequest request) {

        UUID documentId =
                documentIngestionService.ingest(
                        request.title(),
                        request.source(),
                        request.content()
                );

        return ResponseEntity.ok(documentId);
    }

    public record DocumentIngestionRequest(

            @NotBlank
            String title,

            @NotBlank
            String source,

            @NotBlank
            String content
    ) {
    }
}
