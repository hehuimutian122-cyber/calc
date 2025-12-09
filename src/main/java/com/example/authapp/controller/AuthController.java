package com.example.authapp.controller;

import com.example.authapp.dto.UserRegistrationDto;
import com.example.authapp.exception.PasswordMismatchException;
import com.example.authapp.exception.UserAlreadyExistsException;
import com.example.authapp.service.UserService;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "ユーザー名またはパスワードが正しくありません");
        }
        if (logout != null) {
            model.addAttribute("message", "ログアウトしました");
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userRegistrationDto") UserRegistrationDto registrationDto,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register";
        }

        // 例外は@ExceptionHandlerで処理される
        userService.registerNewUser(registrationDto);
        redirectAttributes.addFlashAttribute("success", "ユーザー登録が完了しました。ログインしてください。");
        return "redirect:/login";
    }
    
    /**
     * ユーザー登録時の例外処理
     * フォームのバリデーションエラーとして表示するため、Controller内で処理
     */
    @ExceptionHandler({UserAlreadyExistsException.class, PasswordMismatchException.class})
    public String handleRegistrationException(RuntimeException e, 
                                               @ModelAttribute("userRegistrationDto") UserRegistrationDto registrationDto,
                                               BindingResult result) {
        // フォームのバリデーションエラーとして表示
        result.rejectValue("username", "error.userRegistrationDto", e.getMessage());
        return "register";
    }
}

