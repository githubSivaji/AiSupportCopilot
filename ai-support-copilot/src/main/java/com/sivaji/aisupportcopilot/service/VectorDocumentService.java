package com.sivaji.aisupportcopilot.service;

import com.sivaji.aisupportcopilot.entity.DocumentChunk;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VectorDocumentService {

    private final VectorStore vectorStore;

    public void addChunks(List<DocumentChunk> chunks) {

        List<Document> documents = chunks.stream()
                .map(chunk -> new Document(
                        chunk.getContent(),
                        Map.of(
                                "documentId",
                                chunk.getDocumentId().toString(),
                                "chunkIndex",
                                chunk.getChunkIndex()
                        )
                ))
                .toList();

        vectorStore.add(documents);
    }
}
