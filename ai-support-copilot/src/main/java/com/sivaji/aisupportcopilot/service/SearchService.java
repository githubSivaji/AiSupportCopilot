package com.sivaji.aisupportcopilot.service;

import com.sivaji.aisupportcopilot.entity.DocumentChunk;
import org.springframework.ai.document.Document;

import java.util.List;

public interface SearchService {
    List<DocumentChunk> searchByKeyword(String keyword);
    List<Document> searchBySemantic(String query, int topK);
}
