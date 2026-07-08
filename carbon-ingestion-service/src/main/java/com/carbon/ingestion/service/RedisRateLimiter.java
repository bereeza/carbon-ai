package com.carbon.ingestion.service;

import com.carbon.ingestion.config.ratelimiter.RateLimitProperties;
import com.carbon.shared.exception.RateLimitExceededException;
import com.carbon.shared.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisRateLimiter {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RateLimitProperties rateLimitProperties;
    private static final String RATE_LIMIT_PREFIX = "rate_limit:";

    public RateLimitResult checkRateLimit(String userKey) {
        var redisKey = RATE_LIMIT_PREFIX + userKey;

        try {
            var bucket = getOrCreateBucket(redisKey);

            if (bucket.tryConsume(rateLimitProperties.getSmallTextTokens())) {
                redisTemplate.opsForValue().set(redisKey, bucket, rateLimitProperties.getBucketTtl());
                var remainingTokens = bucket.getRemainingTokens();

                return new RateLimitResult(true, remainingTokens);
            } else {
                redisTemplate.opsForValue().set(redisKey, bucket, rateLimitProperties.getBucketTtl());
                var remainingTokens = bucket.getRemainingTokens();

                throw new RateLimitExceededException(
                        format(
                                "Rate limit exceeded. Available tokens: %.1f, required: %d",
                                remainingTokens,
                                rateLimitProperties.getSmallTextTokens()
                        )
                );
            }
        } catch (Exception e) {
            log.error("Error during rate limit check for user: {}, requestedTokens: {}", userKey, rateLimitProperties.getSmallTextTokens(), e);
            throw new RateLimitExceededException("Rate limiting service unavailable.");
        }
    }

    @SneakyThrows
    private RedisTokenBucket getOrCreateBucket(String redisKey) {
        var bucket = redisTemplate.opsForValue().get(redisKey);
        if (Objects.isNull(bucket)) {
            log.info("No existing bucket found in Redis, creating new one for key: {}", redisKey);
            return new RedisTokenBucket(rateLimitProperties.getBucketCapacity(), rateLimitProperties.getRefillRate());
        }

        if (bucket instanceof RedisTokenBucket redisTokenBucket) {
            log.info("Found existing bucket in Redis for key: {}", redisKey);
            return redisTokenBucket;
        }

        throw new ApplicationException(
                "Failed to deserialize bucket from Redis",
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value()
        );
    }

    public record RateLimitResult(boolean allowed, double remainingTokens) {
    }
}
