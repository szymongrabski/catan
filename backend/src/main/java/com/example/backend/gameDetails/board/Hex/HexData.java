package com.example.backend.gameDetails.board.Hex;

import java.util.Objects;

public class HexData {
    private final HexType type;
    private final HexNumber number;

    public HexData(HexType type, HexNumber number) {
        this.type = type;
        this.number = number;
    }

    public HexType getType() {
        return type;
    }

    public HexNumber getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return String.format("HexData(type=%s, number=%s)", type, number);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HexData hexData = (HexData) o;
        return type == hexData.type && number == hexData.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, number);
    }
}

