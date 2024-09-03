package com.example.backend.gameDetails.board.Vertex;

import com.example.backend.gameDetails.board.Hex.HexData;
import com.example.backend.gameDetails.board.Hex.HexNumber;
import com.example.backend.gameDetails.board.Hex.HexType;
import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private Long boardId;
    private final int q;
    private final int r;
    private final String direction;
    private final List<HexData> hexDataList;


    public Vertex(Long boardId, int q, int r, String direction) {
        this.boardId = boardId;
        this.q = q;
        this.r = r;
        this.direction = direction;
        this.hexDataList = new ArrayList<>();
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

    public void addHexData(HexType type, HexNumber number) {
        hexDataList.add(new HexData(type, number));
    }


    @Override
    public String toString() {
        return String.format("Vertex(q=%d, r=%d, direction=%s)", q, r, direction);
    }
}
