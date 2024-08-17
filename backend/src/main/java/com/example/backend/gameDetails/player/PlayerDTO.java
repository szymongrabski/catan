package com.example.backend.gameDetails.player;


import com.example.backend.user.UserDTO;

public class PlayerDTO {
    private Long id;
    private int points;
    private UserDTO user;

    public PlayerDTO() {}

    public PlayerDTO(Long id, int points, UserDTO user) {
        this.id = id;
        this.points = points;
        this.user = user;
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
}
