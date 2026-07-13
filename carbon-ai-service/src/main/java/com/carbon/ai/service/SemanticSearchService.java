package com.carbon.ai.service;

import com.carbon.ai.config.AnalyticsProperties;
import com.carbon.ai.dto.SearchResponse;
import com.carbon.ai.model.PromptType;
import com.carbon.ai.model.ProcessedContent;
import com.carbon.ai.model.SearchLog;
import com.carbon.ai.repository.ProcessedContentRepository;
import com.carbon.ai.repository.SearchLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SemanticSearchService {

    private final EmbeddingModel embeddingModel;
    private final ChatModel chatModel;
    private final ProcessedContentRepository repository;
    private final SearchLogRepository searchLogRepository;
    private final AnalyticsProperties analyticsProperties;
    private final PromptService promptService;

    @Transactional
    public SearchResponse semanticSearch(String query) {
        return semanticSearch(query, PromptType.STANDARD);
    }

    public SearchResponse semanticSearch(String query, PromptType promptType) {
        logSearchQuery(query);
        
        float[] embeddingLiteral = embeddingModel.embed(query);

        List<ProcessedContent> topMatches = repository.findBySimilarity(embeddingLiteral, PageRequest.of(0, 3));

        if (topMatches.isEmpty()) {
            return SearchResponse.builder()
                    .answer("No relevant context found to answer this question.")
                    .build();
        }

        String context = topMatches.stream()
                .map(ProcessedContent::getSummary)
                .collect(Collectors.joining("\n\n"));

        Map<String, String> variables = Map.of(
            "context", context,
            "query", query
        );

        String prompt = promptService.getPrompt("semantic-search", promptType, variables);

        return SearchResponse.builder()
                .answer(chatModel.call(prompt))
                .build();
    }

    public Map<String, Integer> findTopFrequentWords() {
        var stopWords = Arrays.stream(analyticsProperties.getStopWords())
                .map(word -> "'" + word + "'")
                .collect(Collectors.joining(", "));

        var results = searchLogRepository.getTopFrequentWords(
                analyticsProperties.getRecentSearchesLimit(),
                analyticsProperties.getMinWordLength(),
                stopWords,
                analyticsProperties.getTopWordsLimit()
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
                .userId(UUID.randomUUID().toString())
                .build();

        searchLogRepository.save(searchLog);
    }
}

