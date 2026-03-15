package com.carbon.ai.api;

import com.carbon.ai.dto.SystemInsights;
import com.carbon.ai.service.SystemInsightsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InsightsController implements InsightsAPI {

    private final SystemInsightsService systemInsightsService;

    @Override
    public ResponseEntity<SystemInsights> getInsights() {
        SystemInsights insights = systemInsightsService.getSystemInsights();
        return ResponseEntity.ok(insights);
    }
}
