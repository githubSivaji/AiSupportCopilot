package com.sivaji.aisupportcopilot.ai.agent;

import java.util.UUID;

public interface SupportAgent {

    String chat(String message, UUID userId);
}
