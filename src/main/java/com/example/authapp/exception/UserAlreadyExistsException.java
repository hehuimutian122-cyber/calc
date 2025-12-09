package com.example.authapp.exception;

/**
 * ユーザーが既に存在する場合にスローされる例外
 */
public class UserAlreadyExistsException extends RuntimeException {
    
    public UserAlreadyExistsException() {
        super();
    }
    
    public UserAlreadyExistsException(String message) {
        super(message);
    }
    
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}

