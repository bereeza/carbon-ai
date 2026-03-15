package com.carbon.ingestion.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedisTokenBucket {
    
    @JsonProperty("tokens")
    private double tokens;
    
    @JsonProperty("lastRefillTime")
    private long lastRefillTime;
    
    @JsonProperty("capacity")
    private int capacity;
    
    @JsonProperty("refillRate")
    private double refillRate;
    
    public RedisTokenBucket() {
    }
    
    public RedisTokenBucket(int capacity, double refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefillTime = Instant.now().getEpochSecond();
    }

    public void refill() {
        long now = Instant.now().getEpochSecond();
        long timePassed = now - lastRefillTime;
        
        if (timePassed > 0) {
            double tokensToAdd = timePassed * refillRate / 60.0;
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTime = now;
        }
    }

    public boolean tryConsume(double requestedTokens) {
        refill();
        
        if (tokens >= requestedTokens) {
            tokens -= requestedTokens;
            return true;
        }
        return false;
    }

    public double getRemainingTokens() {
        refill();
        return tokens;
    }
}
