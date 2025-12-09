package com.example.authapp.exception;

/**
 * ユーザーが見つからない場合にスローされる例外
 */
public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException() {
        super();
    }
    
    public UserNotFoundException(String message) {
        super(message);
    }
    
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

