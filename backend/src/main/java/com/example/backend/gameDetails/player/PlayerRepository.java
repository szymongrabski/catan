package com.example.backend.gameDetails.player;

import com.example.backend.gameDetails.game.Game;
import com.example.backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByGameAndUser(Game game, User user);
    List<Player> findByGame(Game game);
    boolean existsByGameAndUser(Game game, User user);
}
