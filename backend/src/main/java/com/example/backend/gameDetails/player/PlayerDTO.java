package com.example.backend.gameDetails.player;


import com.example.backend.gameDetails.board.Hex.HexType;
import com.example.backend.user.UserDTO;

import java.util.Map;

public class PlayerDTO {
    private Long id;
    private int points;
    private UserDTO user;
    private PlayerRole role;
    private Map<HexType, Integer> resources;

    public PlayerDTO() {}

    public PlayerDTO(Long id, int points, PlayerRole role, UserDTO user, Map<HexType, Integer> resources) {
        this.id = id;
        this.points = points;
        this.user = user;
        this.role = role;
        this.resources = resources;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
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
