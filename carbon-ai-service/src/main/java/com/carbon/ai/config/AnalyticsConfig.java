package com.carbon.ai.config;

import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class AnalyticsConfig {

    private static final String[] STOP_WORDS_ARRAY = {
        "the", "a", "an", "is", "are", "was", "were", "be", "been", "being",
        "and", "or", "but", "if", "while", "when", "where", "how", "what", "why", "who",
        "for", "to", "of", "in", "on", "at", "by", "with", "from", "up", "out", "about",
        "this", "that", "these", "those", "i", "you", "he", "she", "it", "we", "they",
        "me", "him", "her", "us", "them", "my", "your", "his", "its", "our", "their",
        "can", "could", "will", "would", "shall", "should", "may", "might", "must",
        "do", "does", "did", "have", "has", "had", "so", "than", "more", "most"
    };

    public static final Set<String> STOP_WORDS = Arrays.stream(STOP_WORDS_ARRAY)
            .collect(Collectors.toSet());

    public static final String STOP_WORDS_SQL_IN_CLAUSE = STOP_WORDS.stream()
            .map(word -> "'" + word + "'")
            .collect(Collectors.joining(", "));

    public static final int MIN_WORD_LENGTH = 3;
    public static final int RECENT_SEARCHES_LIMIT = 1000;
    public static final int TOP_WORDS_LIMIT = 5;
}
