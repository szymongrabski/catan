package com.example.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public UserDTO convertToUserDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    public List<UserDTO> convertToUserDTOList(List<User> users) {
        return users.stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUser() {
        User user = getCurrentUser();
        return convertToUserDTO(user);
    }


    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String currentUsername = ((UserDetails) authentication.getPrincipal()).getUsername();
            User currentUser = userRepository.findUserByUsername(currentUsername)
                    .orElseThrow(() -> new IllegalStateException("User not found"));
            return currentUser.getId();
        } else {
            throw new RuntimeException("User not authenticated");
        }
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String currentUsername = ((UserDetails) authentication.getPrincipal()).getUsername();
            return userRepository.findUserByUsername(currentUsername)
                    .orElseThrow(() -> new IllegalStateException("User not found"));
        } else {
            throw new RuntimeException("User not authenticated");
        }
    }

    public void deleteUser(Long userId) {
        boolean exists = userRepository.existsById(userId);

        if (!exists) {
            throw new IllegalStateException("User with id " + userId + " does not exist");
        }

        userRepository.deleteById(userId);
    }

    public List<Map<String, Object>> findUsersWithFriendshipStatus(String username, Long currentUserId) {
        return userRepository.findUsersWithFriendshipStatus(username, currentUserId);
    }

}
