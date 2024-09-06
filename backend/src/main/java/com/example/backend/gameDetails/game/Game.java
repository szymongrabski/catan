package com.example.backend.gameDetails.game;

import com.example.backend.gameDetails.board.Board;
import com.example.backend.gameDetails.board.Vertex.Vertex;
import com.example.backend.gameDetails.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Game {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);
    private final Long id;

    private List<Player> players;

    private Board board;

    private GameStatus gameStatus;

    private Long currentPlayerIndex;

    private int roundNumber;

    public Game() {
        this.id = ID_GENERATOR.getAndIncrement();
        this.players = new ArrayList<>();
        this.gameStatus = GameStatus.NOT_STARTED;
        this.currentPlayerIndex = 0L;
        this.roundNumber = 0;
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

    public List<Player> getPlayers() {
        return players;
    }

    public Long getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public void addToRoundNumber() {
        this.roundNumber++;
    }


    public void addPlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        this.players.add(player);
    }

    public void startGame() {
        if (gameStatus != GameStatus.NOT_STARTED) {
            throw new IllegalStateException("Game cannot be started because it is already " + gameStatus);
        }

        if (players.size() < 2) {
            throw new IllegalStateException("Cannot start the game. Minimum of two players required.");
        }

        this.gameStatus = GameStatus.IN_PROGRESS;
        Random random = new Random();
        this.currentPlayerIndex = players.get(random.nextInt(players.size())).getId();;
    }
}
