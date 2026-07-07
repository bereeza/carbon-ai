package com.carbon.ai.service;

import com.carbon.ai.dto.SystemInsights;
import com.carbon.ai.repository.ProcessedContentRepository;
import com.carbon.shared.exception.ApplicationException;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SystemInsightsService {

    private final ProcessedContentRepository repository;
    private final ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

    public SystemInsights getSystemInsights() {
        try {
            CompletableFuture<Long> totalDocumentsFuture = CompletableFuture.supplyAsync(
                repository::count, virtualThreadExecutor
            );
            
            CompletableFuture<Double> avgSummaryLengthFuture = CompletableFuture.supplyAsync(
                repository::getAverageSummaryLength, virtualThreadExecutor
            );
            
            CompletableFuture<List<String>> topKeywordsFuture = CompletableFuture.supplyAsync(
                repository::getTopKeywords, virtualThreadExecutor
            );

            Long totalDocuments = totalDocumentsFuture.join();
            Double avgSummaryLength = avgSummaryLengthFuture.join();
            List<String> topKeywords = topKeywordsFuture.join();

            return SystemInsights.builder()
                .totalProcessedDocuments(totalDocuments)
                .averageSummaryLength(avgSummaryLength)
                .topKeywords(topKeywords)
                .build();

        } catch (Exception e) {
            log.error("Error fetching system insights", e);
            throw new ApplicationException(
                    "Failed to fetch system insights",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
        }
    }

    @PreDestroy
    public void destroy() {
        virtualThreadExecutor.shutdown();
    }
}
