package com.carbon.ingestion.service;

import com.carbon.ingestion.exception.ApplicationException;
import com.carbon.ingestion.exception.RateLimitExceededException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

import static com.carbon.ingestion.config.ratelimiter.DefaultLimiterConstant.*;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisRateLimiter {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final int BUCKET_CAPACITY = 10;
    private static final Duration BUCKET_TTL = Duration.ofHours(1);
    private static final String RATE_LIMIT_PREFIX = "rate_limit:";

    public RateLimitResult checkRateLimit(String userKey) {
        var redisKey = RATE_LIMIT_PREFIX + userKey;

        try {
            var bucket = getOrCreateBucket(redisKey);

            if (bucket.tryConsume(SMALL_TEXT_TOKENS.value)) {
                redisTemplate.opsForValue().set(redisKey, bucket, BUCKET_TTL);
                var remainingTokens = bucket.getRemainingTokens();

                return new RateLimitResult(true, remainingTokens);
            } else {
                redisTemplate.opsForValue().set(redisKey, bucket, BUCKET_TTL);
                var remainingTokens = bucket.getRemainingTokens();

                throw new RateLimitExceededException(
                        format("Rate limit exceeded. Available tokens: %.1f, required: %f", remainingTokens, SMALL_TEXT_THRESHOLD.value)
                );
            }
        } catch (Exception e) {
            log.error("Error during rate limit check for user: {}, requestedTokens: {}", userKey, SMALL_TEXT_THRESHOLD.value, e);
            throw new RateLimitExceededException("Rate limiting service unavailable. Please try again later.");
        }
    }

    @SneakyThrows
    private RedisTokenBucket getOrCreateBucket(String redisKey) {
        var bucket = redisTemplate.opsForValue().get(redisKey);
        if (Objects.isNull(bucket)) {
            log.info("No existing bucket found in Redis, creating new one for key: {}", redisKey);
            return new RedisTokenBucket(BUCKET_CAPACITY, REFILL_RATE.value);
        }

        if (bucket instanceof RedisTokenBucket redisTokenBucket) {
            log.info("Found existing bucket in Redis for key: {}", redisKey);
            return redisTokenBucket;
        }

        throw new ApplicationException("Failed to deserialize bucket from Redis");
    }

    public record RateLimitResult(boolean allowed, double remainingTokens) {
    }
}
