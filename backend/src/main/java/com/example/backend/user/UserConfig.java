package com.example.backend.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository repository) {
        return args -> {
            User testUser = new User(
                    "user",
                    "user@example.com",
                    "password"

            );
            User testUser2 = new User(
                    "user2",
                    "user2@example.com",
                    "password"
            );

            repository.saveAll(List.of(testUser, testUser2));
        };
    }
}
