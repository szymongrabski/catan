package com.example.backend.gameDetails.board.Hex;


import com.example.backend.gameDetails.board.Board;
import com.example.backend.gameDetails.board.Vertex.Vertex;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Hex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int q;
    private int r;

    @Enumerated(EnumType.STRING)
    private HexType type;

    @Enumerated(EnumType.STRING)
    private HexNumber number;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "hex_vertex",
            joinColumns = @JoinColumn(name = "hex_id"),
            inverseJoinColumns = @JoinColumn(name = "vertex_id")
    )
    private Set<Vertex> vertices = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;


    public Hex() {
    }

    public Hex(int q, int r, HexType type, HexNumber number) {
        this.q = q;
        this.r = r;
        this.type = type;
        this.number = number;
    }

    public int getQ() {
        return q;
    }

    public int getR() {
        return r;
    }

    public HexType getType() {
        return type;
    }

    public HexNumber getNumber() {
        return number;
    }

    public Set<Vertex> getVertices() {
        return vertices;
    }

    @Override
    public String toString() {
        return String.format("Hex(q=%d, r=%d, %s-%s)", q, r, type, number);
    }
}

