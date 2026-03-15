package com.carbon.ai.api;

import com.carbon.ai.service.SemanticSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AnalyticsController implements AnalyticsAPI {

    private final SemanticSearchService searchService;

    @Override
    public ResponseEntity<Map<String, Integer>> getTrends() {
        Map<String, Integer> wordFrequencyMap = searchService.findTopFrequentWords();
        
        return ResponseEntity.ok(wordFrequencyMap);
    }
}
