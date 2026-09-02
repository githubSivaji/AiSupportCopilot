package com.sivaji.aisupportcopilot.ai.rag;


import com.sivaji.aisupportcopilot.ai.LlmGateway.LlmGateway;
import com.sivaji.aisupportcopilot.dto.SearchResult;
import com.sivaji.aisupportcopilot.service.SearchService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RagServiceImpl implements RagService {

    private final SearchService searchService;
    private final LlmGateway llmGateway;

    private static final int TOP_K = 5;

    @Override
    public String answer(String question) {

        // 1. Retrieve relevant documents
        List<SearchResult> results =
                searchService.hybridSearch(
                        question,
                        TOP_K
                );

        // 2. Build context
        String context = buildContext(results);

        // 3. Build prompt
        String userPrompt = """
                Answer the user's question using only
                the provided context.

                If the answer cannot be found in the context,
                say that you don't have enough information.

                Context:
                %s

                User question:
                %s
                """.formatted(context, question);

        // 4. Send context + question to LLM Gateway
        return llmGateway.chat(
                "You are a helpful customer support assistant.",
                userPrompt
        );
    }

    private String buildContext(
            List<SearchResult> results) {

        if (results.isEmpty()) {
            return "No relevant information was found.";
        }

        StringBuilder context =
                new StringBuilder();

        for (int i = 0; i < results.size(); i++) {

            SearchResult result = results.get(i);

            context.append("Source ")
                    .append(i + 1)
                    .append(":\n");

            context.append(result.getContent())
                    .append("\n\n");
        }

        return context.toString();
    }
}