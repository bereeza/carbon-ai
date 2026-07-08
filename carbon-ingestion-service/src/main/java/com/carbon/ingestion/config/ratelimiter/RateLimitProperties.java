package com.carbon.ingestion.config.ratelimiter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "carbon.ratelimit")
public class RateLimitProperties {
    private int bucketCapacity;
    private Duration bucketTtl;
    private int refillRate;
    private int smallTextTokens;
    private int smallTextThreshold;
}
