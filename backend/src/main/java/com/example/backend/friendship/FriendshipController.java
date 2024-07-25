package com.example.backend.friendship;

import com.example.backend.user.User;
import com.example.backend.user.UserDTO;
import com.example.backend.user.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendshipController {

    private final FriendshipService friendshipService;
    private final UserService userService;

    public FriendshipController(FriendshipService friendshipService, UserService userService) {
        this.friendshipService = friendshipService;
        this.userService = userService;
    }

    @PostMapping("/send")
    public void sendFriendRequest(@RequestParam Long receiverId) {
        friendshipService.sendFriendRequest(receiverId);
    }

    @PostMapping("/respond")
    public void respondToFriendRequest(@RequestParam Long requesterId, @RequestParam boolean accept) {
        friendshipService.respondToFriendRequest(requesterId, accept);
    }

    @GetMapping("/requests")
    public List<UserDTO> getUserFriendRequests() {
        List<User> requests = friendshipService.getUserFriendRequests();
        return userService.convertToUserDTOList(requests);
    }

    @GetMapping()
    public List<UserDTO> getFriends() {
        List<User> friends = friendshipService.getFriends();
        return userService.convertToUserDTOList(friends);
    }
}
