package com.sivaji.aisupportcopilot.service;

import java.util.UUID;

public interface LlmService {
    String generateResponse(String prompt, UUID userId);

}
