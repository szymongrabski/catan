package com.example.backend.gameDetails.board.Hex;

import com.example.backend.gameDetails.board.Vertex.Vertex;
import com.example.backend.gameDetails.board.Vertex.VertexKey;

import java.util.HashMap;
import java.util.Map;

public class Hex {
    private final Long boardId;

    private final int q;

    private final int r;

    private final HexNumber number;

    private final HexType type;

    private final Map<String, Vertex> vertices;

    private static final Map<VertexKey, Vertex> globalVertices = new HashMap<>();

    public Hex(Long boardId, int q, int r, HexType type, HexNumber number) {
        this.boardId = boardId;
        this.q = q;
        this.r = r;
        this.type = type;
        this.number = number;
        this.vertices = new HashMap<>();
        initializeVertices();
    }

    private void initializeVertices() {
        addVertex("N1", q, r, "N");
        addVertex("S1", q, r - 1, "S");
        addVertex("N2", q - 1, r + 1, "N");
        addVertex("S2", q, r, "S");
        addVertex("N3", q, r + 1, "N");
        addVertex("S3", q + 1, r - 1, "S");
    }

    private void addVertex(String key, int q, int r, String direction) {
        Vertex vertex = getOrCreateVertex(q, r, direction);
        vertex.addHexData(this.type, this.number);
        this.vertices.put(key, vertex);
    }


    private Vertex getOrCreateVertex(int q, int r, String direction) {
        VertexKey key = new VertexKey(boardId, q, r, direction);
        return globalVertices.computeIfAbsent(key, k -> new Vertex(boardId, k.q, k.r, k.direction));
    }

    public int getQ() {
        return q;
    }

    public int getR() {
        return r;
    }

    public HexNumber getNumber() {
        return number;
    }

    public HexType getType() {
        return type;
    }

}
