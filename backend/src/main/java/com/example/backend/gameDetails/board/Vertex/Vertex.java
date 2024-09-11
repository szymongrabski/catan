package com.example.backend.gameDetails.board.Vertex;

import com.example.backend.gameDetails.board.Hex.HexData;
import com.example.backend.gameDetails.board.Hex.HexNumber;
import com.example.backend.gameDetails.board.Hex.HexType;
import com.example.backend.gameDetails.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vertex {
    private final int q;
    private final int r;
    private final String direction;
    private final List<HexData> hexDataList;

    private Long ownerId;
    private boolean buildable;
    private boolean isUpgraded;


    public Vertex(int q, int r, String direction) {
        this.q = q;
        this.r = r;
        this.direction = direction;
        this.hexDataList = new ArrayList<>();
        this.ownerId = null;
        this.buildable = true;
        this.isUpgraded = false;
    }

    public boolean isAdjacentTo(Vertex other) {
        int dq = Math.abs(this.q - other.q);
        int dr = Math.abs(this.r - other.r);
        return (dq == 1 && dr == 0) || (dq == 0 && dr == 1) || (dq == 1 && dr == 1);
    }

    public boolean isOccupied() {
        return ownerId != null;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public boolean isBuildable() {
        return buildable;
    }

    public void setBuildable(boolean buildable) {
        this.buildable = buildable;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<HexData> getHexDataList() {
        return hexDataList;
    }

    public boolean isUpgraded() {
        return isUpgraded;
    }

    public void setUpgraded(boolean upgraded) {
        isUpgraded = upgraded;
    }

    @Override
    public String toString() {
        return String.format("Vertex(q=%d, r=%d, direction=%s, ownerId=%s)", q, r, direction, ownerId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return q == vertex.q && r == vertex.r && Objects.equals(direction, vertex.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(q, r, direction);
    }
}
