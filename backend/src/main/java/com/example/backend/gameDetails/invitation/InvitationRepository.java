package com.example.backend.gameDetails.invitation;

import com.example.backend.gameDetails.game.Game;
import com.example.backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Optional<Invitation> findBySenderAndReceiver(User sender, User receiver);
    List<Invitation> findByReceiver(User receiver);
}
