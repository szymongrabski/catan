package com.example.backend.gameDetails.player;


import com.example.backend.gameDetails.board.Hex.HexType;
import com.example.backend.gameDetails.game.Game;
import com.example.backend.user.User;
import jakarta.persistence.*;

import java.util.EnumMap;
import java.util.Map;

@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "game_id")
    private Long gameId;

    @Column(nullable = false)
    private int points;

    @Column
    private PlayerRole role;

    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING)
    @CollectionTable(name = "player_resources", joinColumns = @JoinColumn(name = "player_id"))
    @Column(name = "quantity")
    private Map<HexType, Integer> resources = new EnumMap<>(HexType.class);

    public Player() {
        this.resources.put(HexType.WOOD, 0);
        this.resources.put(HexType.BRICK, 0);
        this.resources.put(HexType.WOOL, 0);
        this.resources.put(HexType.WHEAT, 0);
        this.resources.put(HexType.ROCK, 0);
    }

    public Player(Long gameId, User user) {
        this.gameId = gameId;
        this.user = user;
        this.points = 0;
        this.role = PlayerRole.NORMAL;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public PlayerRole getRole() {
        return role;
    }

    public void setRole(PlayerRole role) {
        this.role = role;
    }

    public Map<HexType, Integer> getResources() {
        return resources;
    }
}

