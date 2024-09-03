package com.example.backend.gameDetails.board.Vertex;

import java.util.Objects;

public class VertexKey {
    public Long boardId;
    public final int q;
    public final int r;
    public final String direction;

    public VertexKey(Long boardId, int q, int r, String direction) {
        this.boardId = boardId;
        this.q = q;
        this.r = r;
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VertexKey that = (VertexKey) o;
        return q == that.q && r == that.r && Objects.equals(direction, that.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(q, r, direction);
    }
}
