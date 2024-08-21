package com.example.backend.friendship;

import com.example.backend.user.User;
import com.example.backend.user.UserRepository;
import com.example.backend.user.UserService;
import com.example.backend.websocket.NotificationsWebSocketHandler;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final NotificationsWebSocketHandler webSocketHandler;

    public FriendshipService(FriendshipRepository friendshipRepository, UserRepository userRepository, UserService userService, NotificationsWebSocketHandler webSocketHandler) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.webSocketHandler = webSocketHandler;
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
            webSocketHandler.notifyUserAboutFriendRequest(receiverId);
        } else {
            throw new RuntimeException("Friend request already sent");
        }
    }

    @Transactional
    public void respondToFriendRequest(Long requesterId, boolean accept) {
        User receiver = userService.getCurrentUser();
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("Requester not found"));

        Friendship friendship = friendshipRepository.findByRequesterAndReceiver(requester, receiver)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        if (accept) {
            friendship.setStatus(FriendshipStatus.ACCEPTED);
            friendshipRepository.save(friendship);
        } else {
            friendshipRepository.delete(friendship);
        }

        webSocketHandler.notifyUserAboutFetchingFriends(requester.getId());
    }

    @Transactional
    public void deleteFriendship(Long friendId) {
        User user = userService.getCurrentUser();
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        Friendship friendship = friendshipRepository.findByRequesterAndReceiver(user, friend)
                .stream()
                .findFirst()
                .orElseGet(() -> friendshipRepository.findByRequesterAndReceiver(friend, user)
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Friendship not found"))
                );

        friendshipRepository.delete(friendship);
        webSocketHandler.notifyUserAboutFetchingFriends(friendId);
    }


    public List<User> getUserFriendRequests() {
        User user = userService.getCurrentUser();
        List<Friendship> friendships = friendshipRepository.findByReceiverIdAndStatus(user.getId(), FriendshipStatus.PENDING);

        return friendships.stream()
                .map(Friendship::getRequester)
                .collect(Collectors.toList());
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
