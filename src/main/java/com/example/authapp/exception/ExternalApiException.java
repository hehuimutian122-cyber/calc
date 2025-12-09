package com.example.authapp.exception;

/**
 * 外部API呼び出しエラーが発生した場合にスローされる例外
 */
public class ExternalApiException extends RuntimeException {
    
    public ExternalApiException() {
        super();
    }
    
    public ExternalApiException(String message) {
        super(message);
    }
    
    public ExternalApiException(String message, Throwable cause) {
        super(message, cause);
    }
}

