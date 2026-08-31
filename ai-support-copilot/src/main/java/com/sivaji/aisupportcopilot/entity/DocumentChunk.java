package com.sivaji.aisupportcopilot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "document_chunks")
public class DocumentChunk {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID documentId;

    @Column(nullable = false)
    private Integer chunkIndex;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

}