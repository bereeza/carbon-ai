package com.carbon.ai.api;

import com.carbon.ai.service.SemanticSearchService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController implements SearchAPI {

    private final SemanticSearchService semanticSearchService;

    @Override
    public ResponseEntity<String> search(
            @Parameter(description = "Search query for semantic search", required = true)
            @RequestParam("query") String query) {
        String answer = semanticSearchService.semanticSearch(query);
        return ResponseEntity.ok(answer);
    }
}

