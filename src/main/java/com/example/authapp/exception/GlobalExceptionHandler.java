package com.example.authapp.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * グローバル例外ハンドラー
 * アプリケーション全体で発生する例外を一元的に処理する
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * UserNotFoundExceptionのハンドリング
     */
    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException e, Model model) {
        logger.error("ユーザーが見つかりませんでした: {}" , e.getMessage(), e);
        model.addAttribute("errorMessage", "指定されたユーザーが見つかりませんでした。");
        return "error/user-not-found";
    }
    
    /**
     * PostalCodeNotFoundExceptionのハンドリング
     */
    @ExceptionHandler(PostalCodeNotFoundException.class)
    public String handlePostalCodeNotFoundException(PostalCodeNotFoundException e, Model model) {
        logger.error("郵便番号が見つかりませんでした: {}" , e.getMessage(), e);
        model.addAttribute("errorMessage", "指定された郵便番号が見つかりませんでした。");
        return "error/postal-code-not-found";
    }
    
    /**
     * InvalidParameterExceptionのハンドリング
     */
    @ExceptionHandler(InvalidParameterException.class)
    public String handleInvalidParameterException(InvalidParameterException e, Model model) {
        logger.error("パラメータが不正です: {}" , e.getMessage(), e);
        model.addAttribute("errorMessage", "入力されたパラメータが不正です。");
        return "error/invalid-parameter";
    }
    
    /**
     * UserAlreadyExistsExceptionのハンドリング
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserAlreadyExistsException(UserAlreadyExistsException e, Model model) {
        logger.warn("ユーザーが既に存在します: {}" , e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return "error/user-already-exists";
    }
    
    /**
     * PasswordMismatchExceptionのハンドリング
     */
    @ExceptionHandler(PasswordMismatchException.class)
    public String handlePasswordMismatchException(PasswordMismatchException e, Model model) {
        logger.warn("パスワードが一致しません: {}" , e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return "error/password-mismatch";
    }
    
    /**
     * ExternalApiExceptionのハンドリング
     * 外部API呼び出しエラーが発生した場合の処理
     */
    @ExceptionHandler(ExternalApiException.class)
    public String handleExternalApiException(ExternalApiException e, Model model) {
        logger.error("外部API呼び出しエラー: {}" , e.getMessage(), e);
        model.addAttribute("errorMessage", "外部サービスへの接続に失敗しました。しばらく時間をおいて再度お試しください。");
        return "error/external-api";
    }
    
    /**
     * IllegalArgumentExceptionのハンドリング
     * カスタム例外に移行するまでの暫定対応として追加
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        logger.error("不正な引数が渡されました: {}" , e.getMessage(), e);
        model.addAttribute("errorMessage", e.getMessage());
        return "error/invalid-parameter";
    }
    
    /**
     * その他の予期しない例外のハンドリング
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        logger.error("予期しないエラー: {}" , e.getMessage(), e);
        model.addAttribute("errorMessage", "予期しないエラーが発生しました。管理者に問い合わせください。");
        return "error/general";
    }
}

