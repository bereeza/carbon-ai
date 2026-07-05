package com.carbon.shared.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(
            ApplicationException e,
            HttpServletRequest request
    ) {
        log.warn("ApplicationException: {}", e.getMessage());
        ErrorResponse body = ErrorResponse.of(
                e.getStatus(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(e.getStatus()).body(body);
    }

    @ExceptionHandler(AccessDeniedCustomException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            Exception e,
            HttpServletRequest request
    ) {
        log.warn("AccessDeniedException: {}", e.getMessage());
        ErrorResponse body = ErrorResponse.of(
                HttpStatus.FORBIDDEN, e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleRateLimiterException(
            RateLimitExceededException e,
            HttpServletRequest request
    ) {
        log.error("RateLimiterException: {}", e.getMessage());
        ErrorResponse body = ErrorResponse.of(
                HttpStatus.TOO_MANY_REQUESTS, e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(body);
    }
}
