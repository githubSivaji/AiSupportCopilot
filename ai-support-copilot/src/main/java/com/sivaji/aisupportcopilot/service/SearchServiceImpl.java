package com.sivaji.aisupportcopilot.service;

import com.sivaji.aisupportcopilot.dto.SearchResult;
import com.sivaji.aisupportcopilot.entity.DocumentChunk;
import com.sivaji.aisupportcopilot.repository.DocumentChunkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
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

    @Override
    public List<SearchResult> hybridSearch(String query, int topK) {
        List<DocumentChunk> keywordResults =
                searchByKeyword(query);

        List<Document> semanticResults =
                searchBySemantic(query, topK);

        List<SearchResult> results = new ArrayList<>();

        /*
         * Keyword results
         */
        for (DocumentChunk chunk : keywordResults) {

            results.add(
                    new SearchResult(
                            chunk.getContent(),
                            0.4,
                            "keyword"
                    )
            );
        }
        /*
         * Semantic results
         */
        for (Document document : semanticResults) {

            double similarityScore =
                    extractSimilarityScore(document);

            results.add(
                    new SearchResult(
                            document.getText(),
                            similarityScore * 0.6,
                            "semantic"
                    )
            );
        }


        return results.stream()
                .sorted(
                        Comparator.comparingDouble(
                                SearchResult::getScore
                        ).reversed()
                )
                .limit(topK)
                .toList();
    }
    private double extractSimilarityScore(
            Document document) {

        Object score =
                document.getMetadata().get("distance");

        if (score instanceof Number number) {

            double distance = number.doubleValue();

            // Cosine distance:
            // smaller distance = more similar
            return 1.0 - distance;
        }

        return 0.0;
    }
}