package com.example.backend.friendship;

import com.example.backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByRequesterOrReceiver(User user1, User user2);

    Optional<Friendship> findByRequesterAndReceiver(User requester, User receiver);

    List<Friendship> findByRequesterIdAndStatus(Long userId, FriendshipStatus status);

    List<Friendship> findByReceiverIdAndStatus(Long userId, FriendshipStatus status);
}
