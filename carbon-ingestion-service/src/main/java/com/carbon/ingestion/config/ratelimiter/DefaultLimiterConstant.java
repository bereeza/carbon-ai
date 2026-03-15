package com.carbon.ingestion.config.ratelimiter;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum DefaultLimiterConstant {
    SMALL_TEXT_TOKENS(2),
    SMALL_TEXT_THRESHOLD(1024),
    REFILL_RATE(2);

    public final double value;
}
