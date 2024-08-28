package com.example.backend.gameDetails.game;

import com.example.backend.gameDetails.board.Board;
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "board_id")
    private Board board;

    public Game() {}

    public Game(List<Player> players, Board board) {
        this.players = players;
        this.board = board;
    }

    public Long getId() {
        return id;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

}
