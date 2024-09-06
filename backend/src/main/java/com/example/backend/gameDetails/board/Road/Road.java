package com.example.backend.gameDetails.board.Road;

import com.example.backend.gameDetails.board.Vertex.Vertex;
import com.example.backend.gameDetails.player.Player;

public class Road {
    private Vertex startVertex;
    private Vertex endVertex;
    private Player owner;

    public Road(Vertex startVertex, Vertex endVertex, Player owner) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        this.owner = owner;
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

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
