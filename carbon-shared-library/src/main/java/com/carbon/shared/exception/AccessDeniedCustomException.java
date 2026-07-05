package com.carbon.shared.exception;

import org.springframework.http.HttpStatus;

public class AccessDeniedCustomException extends ApplicationException {
    public AccessDeniedCustomException(String message) {
        super(message, HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value());
    }
}
