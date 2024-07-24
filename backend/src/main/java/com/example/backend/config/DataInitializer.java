package com.example.backend.config;

import com.example.backend.friendship.FriendshipRepository;
import com.example.backend.user.Role;
import com.example.backend.user.User;
import com.example.backend.user.UserRepository;
import com.example.backend.friendship.Friendship;
import com.example.backend.friendship.FriendshipStatus;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, FriendshipRepository friendshipRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (userRepository.count() == 0) {
            User user1 = new User("user", "user@example.com", passwordEncoder.encode("password"), Role.ROLE_USER);
            User user2 = new User("user2", "user2@example.com", passwordEncoder.encode("password2"), Role.ROLE_USER);
            User user3 = new User("user3", "user3@example.com", passwordEncoder.encode("password3"), Role.ROLE_USER);
            userRepository.saveAll(List.of(user1, user2, user3));
        }

        User user1 = userRepository.findUserByUsername("user").orElseThrow();
        User user2 = userRepository.findUserByUsername("user2").orElseThrow();
        User user3 = userRepository.findUserByUsername("user3").orElseThrow();

        Friendship friendship1 = new Friendship(user1, user2, FriendshipStatus.ACCEPTED);
        Friendship friendship2 = new Friendship(user2, user3, FriendshipStatus.PENDING);

        friendshipRepository.saveAll(List.of(friendship1, friendship2));
    }
}
