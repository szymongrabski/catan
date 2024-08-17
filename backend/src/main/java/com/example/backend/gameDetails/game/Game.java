package com.example.backend.gameDetails.game;

import com.example.backend.gameDetails.player.Player;
import jakarta.persistence.*;

        import java.util.List;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long id;

    @OneToMany
    private List<Player> players;

    public Game() {}

    public Game(List<Player> players) {
        this.players = players;
    }

    public Long getId() {
        return id;
    }

}
