package com.carbon.shared.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {

    private final HttpStatus status;
    private final int errorCode;

    public ApplicationException(String message, HttpStatus status, int errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
}