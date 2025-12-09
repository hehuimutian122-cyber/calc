package com.example.authapp.config;

import com.example.authapp.entity.User;
import com.example.authapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // 管理者ユーザーが存在しない場合、作成する
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.addRole(User.Role.ADMIN);
            admin.addRole(User.Role.USER);
            userRepository.save(admin);
            System.out.println("管理者ユーザーを作成しました: admin / admin123");
        }

        // テストユーザーが存在しない場合、作成する
        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setEmail("user@example.com");
            user.addRole(User.Role.USER);
            userRepository.save(user);
            System.out.println("テストユーザーを作成しました: user / user123");
        }
    }
}

