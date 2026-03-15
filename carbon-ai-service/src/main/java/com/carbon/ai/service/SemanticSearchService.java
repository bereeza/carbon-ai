package com.carbon.ai.service;

import com.carbon.ai.config.AnalyticsConfig;
import com.carbon.ai.model.ProcessedContent;
import com.carbon.ai.model.SearchLog;
import com.carbon.ai.repository.ProcessedContentRepository;
import com.carbon.ai.repository.SearchLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SemanticSearchService {

    private final EmbeddingModel embeddingModel;
    private final ChatModel chatModel;
    private final ProcessedContentRepository repository;
    private final SearchLogRepository searchLogRepository;

    @Transactional
    public String semanticSearch(String query) {
        logSearchQuery(query);
        
        float[] queryEmbedding = embeddingModel.embed(query);

        String embeddingLiteral = toPgVectorLiteral(queryEmbedding);

        List<ProcessedContent> topMatches = repository.findTop3BySimilarity(embeddingLiteral);

        if (topMatches.isEmpty()) {
            return "No relevant context found to answer this question.";
        }

        String context = topMatches.stream()
                .map(ProcessedContent::getSummary)
                .collect(Collectors.joining("\n\n"));

        String prompt = """
                Based on the following context, please answer the user's question.

                Context:
                %s

                Question:
                %s
                """.formatted(context, query);

        return chatModel.call(prompt);
    }

    public Map<String, Integer> findTopFrequentWords() {
        var results = searchLogRepository.getTopFrequentWords(
                AnalyticsConfig.RECENT_SEARCHES_LIMIT,
                AnalyticsConfig.MIN_WORD_LENGTH,
                AnalyticsConfig.STOP_WORDS_SQL_IN_CLAUSE,
                AnalyticsConfig.TOP_WORDS_LIMIT
        );

        Map<String, Integer> wordFrequencyMap = new HashMap<>();

        for (Object[] result : results) {
            String word = (String) result[0];
            Number frequency = (Number) result[1];
            wordFrequencyMap.put(word, frequency.intValue());
        }

        return wordFrequencyMap;
    }

    private void logSearchQuery(String query) {
        SearchLog searchLog = SearchLog.builder()
                .query(query)
                .searchedAt(LocalDateTime.now())
                .build();
        searchLogRepository.save(searchLog);
    }

    private String toPgVectorLiteral(float[] embedding) {
        if (embedding == null || embedding.length == 0) {
            throw new IllegalArgumentException("Embedding must not be null or empty");
        }
        return Arrays.toString(embedding);
    }
}

