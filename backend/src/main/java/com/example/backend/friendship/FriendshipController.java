package com.example.backend.friendship;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendshipController {

    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @PostMapping("/send")
    public void sendFriendRequest(@RequestParam Long receiverId) {
        friendshipService.sendFriendRequest(receiverId);
    }

    @PostMapping("/respond")
    public void respondToFriendRequest(@RequestParam Long receiverId, @RequestParam boolean accept) {
        friendshipService.respondToFriendRequest(receiverId, accept);
    }

    @GetMapping("/requests")
    public List<Friendship> getUserFriendRequests(@RequestParam Long userId) {
        return friendshipService.getUserFriendRequests();
    }
}
