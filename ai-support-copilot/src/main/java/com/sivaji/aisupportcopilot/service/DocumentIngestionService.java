package com.sivaji.aisupportcopilot.service;

import java.util.UUID;

public interface DocumentIngestionService {
    UUID ingest(
            String title,
            String source,
            String content
    );
}
