package com.example.authapp.exception;

/**
 * パラメータが不正な場合にスローされる例外
 */
public class InvalidParameterException extends RuntimeException {
    
    public InvalidParameterException() {
        super();
    }
    
    public InvalidParameterException(String message) {
        super(message);
    }
    
    public InvalidParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}

