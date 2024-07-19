package com.example.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void addNewUser(User user) {
        Optional<User> usersByEmail = userRepository.findUserByEmail(user.getEmail());
        Optional<User> usersByUsername = userRepository.findUserByUsername(user.getUsername());

        if (usersByEmail.isPresent()) {
            throw new IllegalStateException("User with email " + user.getEmail() + " already exists");
        }

        if (usersByUsername.isPresent()) {
            throw new IllegalStateException("User with username " + user.getUsername() + " already exists");
        }

        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        boolean exists = userRepository.existsById(userId);

        if (!exists) {
            throw new IllegalStateException("User with id " + userId + " does not exist");
        }

        userRepository.deleteById(userId);
    }
}
