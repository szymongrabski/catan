package com.example.backend.friendship;

import com.example.backend.user.User;
import com.example.backend.user.UserRepository;
import com.example.backend.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public FriendshipService(FriendshipRepository friendshipRepository, UserRepository userRepository, UserService userService) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Transactional
    public void sendFriendRequest(Long receiverId) {
        User requester = userService.getCurrentUser();
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (friendshipRepository.findByRequesterAndReceiver(requester, receiver).isEmpty()) {
            Friendship friendship = new Friendship();
            friendship.setRequester(requester);
            friendship.setReceiver(receiver);
            friendship.setStatus(FriendshipStatus.PENDING);
            friendshipRepository.save(friendship);
        } else {
            throw new RuntimeException("Friend request already sent");
        }
    }

    @Transactional
    public void respondToFriendRequest(Long receiverId, boolean accept) {
        User requester = userService.getCurrentUser();
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Friendship friendship = friendshipRepository.findByRequesterAndReceiver(requester, receiver)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        if (accept) {
            friendship.setStatus(FriendshipStatus.ACCEPTED);
            friendshipRepository.save(friendship);
        } else {
            friendshipRepository.delete(friendship);
        }
    }

    public List<Friendship> getUserFriendRequests() {
        User user = userService.getCurrentUser();

        return friendshipRepository.findByRequesterOrReceiver(user, user);
    }

    public List<User> getFriends() {
        Long userId = userService.getCurrentUserId();
        List<Friendship> requesterFriendships = friendshipRepository.findByRequesterIdAndStatus(userId, FriendshipStatus.ACCEPTED);

        List<Friendship> receiverFriendships = friendshipRepository.findByReceiverIdAndStatus(userId, FriendshipStatus.ACCEPTED);

        Set<User> friends = new HashSet<>();
        friends.addAll(requesterFriendships.stream().map(Friendship::getReceiver).toList());
        friends.addAll(receiverFriendships.stream().map(Friendship::getRequester).toList());

        return new ArrayList<>(friends);
    }
}
