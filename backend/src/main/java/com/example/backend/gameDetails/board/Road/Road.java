package com.example.backend.gameDetails.board.Road;

import com.example.backend.gameDetails.board.Vertex.Vertex;
import com.example.backend.gameDetails.player.Player;

public class Road {
    private Vertex startVertex;
    private Vertex endVertex;
    private Long ownerId;

    public Road(Vertex startVertex, Vertex endVertex, Long owner) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        this.ownerId = owner;
    }

    public Vertex getStartVertex() {
        return startVertex;
    }

    public void setStartVertex(Vertex startVertex) {
        this.startVertex = startVertex;
    }

    public Vertex getEndVertex() {
        return endVertex;
    }

    public void setEndVertex(Vertex endVertex) {
        this.endVertex = endVertex;
    }

    public boolean isOccupied() {
        return ownerId != null;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
