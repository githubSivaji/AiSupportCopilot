package com.sivaji.aisupportcopilot.service;

import com.sivaji.aisupportcopilot.entity.Document;
import com.sivaji.aisupportcopilot.entity.DocumentChunk;


import com.sivaji.aisupportcopilot.repository.DocumentChunkRepository;
import com.sivaji.aisupportcopilot.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentIngestionServiceImpl
        implements DocumentIngestionService {

    private final DocumentRepository documentRepository;
    private final DocumentChunkRepository documentChunkRepository;
    private final VectorDocumentService vectorDocumentService;

    private static final int CHUNK_SIZE = 500;

    @Override
    @Transactional
    public UUID ingest(
            String title,
            String source,
            String content) {

        // 1. Save document
        Document document = Document.builder()
                .title(title)
                .source(source)
                .content(content)
                .build();

        Document savedDocument =
                documentRepository.save(document);

        // 2. Split content into chunks
        List<String> chunks =
                splitIntoChunks(content);

        // 3. Save chunks
        List<DocumentChunk> documentChunks =
                new ArrayList<>();

        for (int i = 0; i < chunks.size(); i++) {

            DocumentChunk chunk =
                    DocumentChunk.builder()
                            .documentId(savedDocument.getId())
                            .chunkIndex(i)
                            .content(chunks.get(i))
                            .build();

            documentChunks.add(chunk);
        }

        documentChunkRepository.saveAll(documentChunks);
        vectorDocumentService.addChunks(documentChunks);

        return savedDocument.getId();
    }

    private List<String> splitIntoChunks(String content) {

        List<String> chunks = new ArrayList<>();

        for (int start = 0;
             start < content.length();
             start += CHUNK_SIZE) {

            int end = Math.min(
                    start + CHUNK_SIZE,
                    content.length()
            );

            chunks.add(
                    content.substring(start, end)
            );
        }

        return chunks;
    }
}
