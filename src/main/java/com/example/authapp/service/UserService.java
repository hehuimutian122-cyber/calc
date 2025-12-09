package com.example.authapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.authapp.dto.UserRegistrationDto;
import com.example.authapp.entity.User;
import com.example.authapp.exception.PasswordMismatchException;
import com.example.authapp.exception.UserAlreadyExistsException;
import com.example.authapp.exception.UserNotFoundException;
import com.example.authapp.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerNewUser(UserRegistrationDto registrationDto) {
        // ユーザー名の重複チェック
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new UserAlreadyExistsException("このユーザー名は既に使用されています");
        }
        
        // メールアドレスの重複チェック
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new UserAlreadyExistsException("このメールアドレスは既に使用されています");
        }
        
        // パスワード確認
        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            throw new PasswordMismatchException("パスワードが一致しません");
        }
        
        // 新しいユーザーを作成
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setEmail(registrationDto.getEmail());
        user.addRole(User.Role.USER);
        
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
    	//Optionalでnullチェックを省略
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(username);
        }
        return userOpt.get();
    }
}

