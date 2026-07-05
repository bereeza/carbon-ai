package com.carbon.shared.exception;

import org.springframework.http.HttpStatus;

public class RateLimitExceededException extends ApplicationException {

    public RateLimitExceededException(String message) {
        super(message, HttpStatus.TOO_MANY_REQUESTS, HttpStatus.TOO_MANY_REQUESTS.value());
    }
}
