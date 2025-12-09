package com.example.authapp.exception;

/**
 * 郵便番号が見つからない場合にスローされる例外
 */
public class PostalCodeNotFoundException extends RuntimeException {
    
    public PostalCodeNotFoundException() {
        super();
    }
    
    public PostalCodeNotFoundException(String message) {
        super(message);
    }
    
    public PostalCodeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

