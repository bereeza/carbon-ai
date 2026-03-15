package com.carbon.ingestion.config.ratelimiter;

import com.carbon.ingestion.exception.RateLimitExceededException;
import com.carbon.ingestion.service.RedisRateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;

import static org.springframework.http.HttpHeaders.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RedisRateLimiter redisRateLimiter;
    private static final String KEY_PREFIX = "ip:";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!request.getRequestURI().startsWith("/api/v1/ingest")) {
            return true;
        }

        var userKey = KEY_PREFIX + request.getRemoteAddr();
        
        try {
            var result = redisRateLimiter.checkRateLimit(userKey);
            response.setHeader("X-RateLimit-Remaining", String.valueOf(result.remainingTokens()));
            
            return true;
        } catch (RateLimitExceededException e) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            response.setHeader(CACHE_CONTROL, "no-cache, no-store, must-revalidate");
            response.setHeader(PRAGMA, "no-cache");
            response.setHeader(EXPIRES, "0");
            
            var errorResponse = String.format(
                "{\"status\": %d, \"error\": \"Rate limit exceeded\", \"message\": \"%s\"}",
                HttpStatus.TOO_MANY_REQUESTS.value(), e.getMessage().replace("\"", "\\\""));
            
            try {
                response.getWriter().write(errorResponse);
                response.getWriter().flush();
                response.getWriter().close();
            } catch (Exception ex) {
                log.error("Failed to write rate limit error response", ex);
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                return false;
            }
            
            return false;
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return false;
        }
    }
}
