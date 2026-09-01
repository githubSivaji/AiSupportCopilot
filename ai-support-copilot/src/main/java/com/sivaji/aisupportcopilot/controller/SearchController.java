package com.sivaji.aisupportcopilot.controller;

import com.sivaji.aisupportcopilot.dto.SearchResult;
import com.sivaji.aisupportcopilot.entity.DocumentChunk;
import com.sivaji.aisupportcopilot.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.document.Document;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<List<DocumentChunk>> search(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                searchService.searchByKeyword(keyword)
        );
    }
    @GetMapping("/semantic")
    public ResponseEntity<List<Document>> semanticSearch(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int topK) {

        return ResponseEntity.ok(
                searchService.searchBySemantic(query, topK)
        );
    }
    @GetMapping("/hybrid")
    public ResponseEntity<List<SearchResult>> hybridSearch(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int topK) {

        return ResponseEntity.ok(
                searchService.hybridSearch(query, topK)
        );
    }
}