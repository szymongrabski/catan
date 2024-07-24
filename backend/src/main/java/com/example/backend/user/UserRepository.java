package com.example.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.username = ?1")
    Optional<User> findUserByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:username%")
    List<User> findByUsernameContaining(@Param("username") String username);

    @Query(value = "SELECT u.user_id AS userId, u.username AS username, COALESCE(f.status, 'NONE') AS friendship_status " +
            "FROM users u " +
            "LEFT JOIN friendships f ON (f.requester_id = u.user_id AND f.receiver_id = :currentUserId) " +
            "OR (f.receiver_id = u.user_id AND f.requester_id = :currentUserId) " +
            "WHERE u.username LIKE %:username% AND u.user_id != :currentUserId",
            nativeQuery = true)
    List<Map<String, Object>> findUsersWithFriendshipStatus(@Param("username") String username, @Param("currentUserId") Long currentUserId);

}
