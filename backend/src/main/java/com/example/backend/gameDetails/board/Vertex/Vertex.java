package com.example.backend.gameDetails.board.Vertex;

import com.example.backend.gameDetails.board.Hex.HexData;
import com.example.backend.gameDetails.board.Hex.HexNumber;
import com.example.backend.gameDetails.board.Hex.HexType;
import com.example.backend.gameDetails.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private final int q;
    private final int r;
    private final String direction;
    private final List<HexData> hexDataList;

    private Player owner;
    private boolean buildable;


    public Vertex(int q, int r, String direction) {
        this.q = q;
        this.r = r;
        this.direction = direction;
        this.hexDataList = new ArrayList<>();
        this.owner = null;
        this.buildable = true;
    }

    public boolean isAdjacentTo(Vertex other) {
        int dq = Math.abs(this.q - other.q);
        int dr = Math.abs(this.r - other.r);
        return (dq == 1 && dr == 0) || (dq == 0 && dr == 1) || (dq == 1 && dr == 1);
    }

    public boolean isOccupied() {
        return owner != null;
    }

    public void addHexData(HexType type, HexNumber number) {
        hexDataList.add(new HexData(type, number));
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

    public Player getOwner() {
        return owner;
    }

    public boolean isBuildable() {
        return buildable;
    }

    public void setBuildable(boolean buildable) {
        this.buildable = buildable;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return String.format("Vertex(q=%d, r=%d, direction=%s, owner=%s)", q, r, direction, owner);
    }
}
