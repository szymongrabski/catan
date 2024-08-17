package com.example.backend.gameDetails;

public enum HexNumber {
    ZERO(0), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), EIGHT(8), NINE(9), TEN(10), ELEVEN(11), TWELVE(12);

    private final int value;

    HexNumber(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static HexNumber fromValue(int value) {
        for (HexNumber number : values()) {
            if (number.value == value) {
                return number;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
