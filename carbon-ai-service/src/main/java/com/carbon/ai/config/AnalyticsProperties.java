package com.carbon.ai.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "carbon.analytics")
public class AnalyticsProperties {
    private String[] stopWords;
    private int minWordLength;
    private int recentSearchesLimit;
    private int topWordsLimit;
}
