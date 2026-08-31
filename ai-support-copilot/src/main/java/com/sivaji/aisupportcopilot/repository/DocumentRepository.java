package com.sivaji.aisupportcopilot.repository;

import com.sivaji.aisupportcopilot.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
}
