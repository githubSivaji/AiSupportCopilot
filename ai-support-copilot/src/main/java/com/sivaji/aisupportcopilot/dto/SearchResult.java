package com.sivaji.aisupportcopilot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchResult {

    private final String content;
    private final double score;
    private final String source;
}