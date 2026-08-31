package com.sivaji.aisupportcopilot.repository;

import com.sivaji.aisupportcopilot.entity.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, UUID> {
    List<DocumentChunk> findByDocumentIdOrderByChunkIndex(
            UUID documentId
    );
    @Query(value = """
            SELECT *
            FROM DOCUMENT_CHUNKS
            WHERE LOWER(CONTENT) LIKE LOWER(CONCAT('%', :keyword, '%'))
            ORDER BY CHUNK_INDEX
            """,
            nativeQuery = true)
    List<DocumentChunk> searchByKeyword(
            @Param("keyword") String keyword
    );

}
