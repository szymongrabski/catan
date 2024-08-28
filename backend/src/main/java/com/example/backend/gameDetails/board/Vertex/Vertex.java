package com.example.backend.gameDetails.board.Vertex;


import com.example.backend.gameDetails.board.Hex.Hex;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vertices")
public class Vertex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int q;
    private int r;
    private String direction;

    @ManyToMany(mappedBy = "vertices")
    private List<Hex> hexes = new ArrayList<>();


    public Vertex() {}

    public Vertex(int q, int r, String direction) {
        this.q = q;
        this.r = r;
        this.direction = direction;
    }

    public void addHex(Hex hex) {
        this.hexes.add(hex);
    }

    public List<Hex> getHexes() {
        return hexes;
    }

    public int getQ() {
        return q;
    }

    public int getR() {
        return r;
    }

    public String getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return String.format("Vertex(q=%d, r=%d, direction=%s)", q, r, direction);
    }
}
