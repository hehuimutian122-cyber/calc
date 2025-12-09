package com.example.authapp.exception;

/**
 * パスワードが一致しない場合にスローされる例外
 */
public class PasswordMismatchException extends RuntimeException {
    
    public PasswordMismatchException() {
        super();
    }
    
    public PasswordMismatchException(String message) {
        super(message);
    }
    
    public PasswordMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}

