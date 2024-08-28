package com.example.backend.gameDetails.board;

import com.example.backend.gameDetails.board.Hex.Hex;
import com.example.backend.gameDetails.board.Hex.HexNumber;
import com.example.backend.gameDetails.board.Hex.HexType;
import com.example.backend.gameDetails.game.Game;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;


@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "board")
    private List<Hex> hexes;

    @Transient
    public final EnumMap<HexType, Integer> hexTypeCounts;

    @Transient
    public final EnumMap<HexNumber, Integer> hexNumberCounts;

    @Transient
    public final EnumMap<HexType, Integer> hexTypeLimits;

    @Transient
    public final EnumMap<HexNumber, Integer> hexNumberLimits;

    @Transient
    public final Random random = new Random();

    @OneToOne(mappedBy = "board")
    private Game game;

    public Board() {
        this.hexTypeLimits = new EnumMap<>(HexType.class);
        this.hexNumberLimits = new EnumMap<>(HexNumber.class);

        this.hexTypeCounts = new EnumMap<>(HexType.class);
        this.hexNumberCounts = new EnumMap<>(HexNumber.class);

        initializeLimits();

        hexes = new ArrayList<>();
    }

    private void initializeLimits() {
        hexTypeLimits.put(HexType.WOOD, 4);
        hexTypeLimits.put(HexType.BRICK, 3);
        hexTypeLimits.put(HexType.WOOL, 4);
        hexTypeLimits.put(HexType.WHEAT, 4);
        hexTypeLimits.put(HexType.ROCK, 3);
        hexTypeLimits.put(HexType.EMPTY, 1);

        hexNumberLimits.put(HexNumber.TWO, 1);
        hexNumberLimits.put(HexNumber.THREE, 2);
        hexNumberLimits.put(HexNumber.FOUR, 2);
        hexNumberLimits.put(HexNumber.FIVE, 2);
        hexNumberLimits.put(HexNumber.SIX, 2);
        hexNumberLimits.put(HexNumber.EIGHT, 2);
        hexNumberLimits.put(HexNumber.NINE, 2);
        hexNumberLimits.put(HexNumber.TEN, 2);
        hexNumberLimits.put(HexNumber.ELEVEN, 2);
        hexNumberLimits.put(HexNumber.TWELVE, 1);
    }


    public List<Hex> getHexes() {
        return hexes;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}
