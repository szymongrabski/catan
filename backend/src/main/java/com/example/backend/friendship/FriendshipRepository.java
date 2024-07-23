package com.example.backend.friendship;

import com.example.backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByRequesterOrReceiver(User user1, User user2);
    Optional<Friendship> findByRequesterAndReceiver(User requester, User receiver);
}
