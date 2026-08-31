package com.sivaji.aisupportcopilot.service;

import com.sivaji.aisupportcopilot.entity.DocumentChunk;
import com.sivaji.aisupportcopilot.repository.DocumentChunkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchServiceImpl implements SearchService {

    private final DocumentChunkRepository documentChunkRepository;
    private final VectorStore vectorStore;

    @Override
    public List<DocumentChunk> searchByKeyword(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        return documentChunkRepository
                .searchByKeyword(keyword.trim());
    }

    @Override
    public List<Document> searchBySemantic(
            String query,
            int topK) {

        return vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(topK)
                        .build()
        );
    }
}