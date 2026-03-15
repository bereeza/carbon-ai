package com.carbon.ai.exception;

public class ApplicationException extends RuntimeException {

    public  ApplicationException(String message) {
        super(message);
    }

    public  ApplicationException(String message, Exception e) {
        super(message, e);
    }
}
