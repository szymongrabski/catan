package com.example.backend.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class UserConfig {
    private final PasswordEncoder passwordEncoder;

    public UserConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository repository) {
        return args -> {
            User testUser = new User(
                    "user",
                    "user@example.com",
                    passwordEncoder.encode("password"),
                    Role.ROLE_USER

            );
            User testUser2 = new User(
                    "user2",
                    "user2@example.com",
                    passwordEncoder.encode("password2"),
                    Role.ROLE_USER
            );

            repository.saveAll(List.of(testUser, testUser2));
        };
    }
}
