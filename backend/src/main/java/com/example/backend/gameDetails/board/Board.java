package com.example.backend.gameDetails.board;


import com.example.backend.gameDetails.board.Hex.Hex;
import com.example.backend.gameDetails.board.Hex.HexNumber;
import com.example.backend.gameDetails.board.Hex.HexType;
import com.example.backend.gameDetails.board.Road.Road;
import com.example.backend.gameDetails.board.Vertex.Vertex;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Board {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    private final Long id;
    private final List<Hex> hexes;
    private final List<Vertex> vertices;
    private final List<Road> roads;

    private final EnumMap<HexType, Integer> hexTypeCounts;
    private final EnumMap<HexNumber, Integer> hexNumberCounts;
    private final EnumMap<HexType, Integer> hexTypeLimits;
    private final EnumMap<HexNumber, Integer> hexNumberLimits;

    private final Random random = new Random();

    public Board() {
        this.id = ID_GENERATOR.getAndIncrement();;
        this.hexes = new ArrayList<>();
        this.hexTypeLimits = new EnumMap<>(HexType.class);
        this.hexNumberLimits = new EnumMap<>(HexNumber.class);

        this.hexTypeCounts = new EnumMap<>(HexType.class);
        this.hexNumberCounts = new EnumMap<>(HexNumber.class);

        this.vertices = new ArrayList<>();
        this.roads = new ArrayList<>();

        initializeLimits();
        initializeHexes();
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


    private void initializeHexes() {
        int[] rowSizes = {3, 4, 5, 4, 3};
        int numRows = rowSizes.length;

        for (int r = 0; r < numRows; r++) {
            int rowSize = rowSizes[r];
            int startQ = switch (r) {
                case 0 -> 0;
                case 1 -> -1;
                case 2 -> -2;
                default -> -2;
            };

            for (int q = 0; q < rowSize; q++) {
                int actualQ = startQ + q;

                HexType type = getNextHexType();
                Hex hex;
                if (type == HexType.EMPTY) {
                    hex = new Hex(this, actualQ, r, type, HexNumber.ZERO);
                } else {
                    HexNumber number = getNextHexNumber();
                    hex = new Hex(this, actualQ, r, type, number);
                }
                hexes.add(hex);
            }
        }
    }

    public List<Vertex> getAdjacentVertices(Vertex vertex) {
        List<Vertex> adjacentVertices = new ArrayList<>();
        int q = vertex.getQ();
        int r = vertex.getR();
        String direction = vertex.getDirection();

        switch (direction) {
            case "N":
                addIfExists(adjacentVertices, q, r - 1, "S");
                addIfExists(adjacentVertices, q + 1, r - 2, "S");
                addIfExists(adjacentVertices, q + 1, r - 1, "S");
                break;
            case "S":
                addIfExists(adjacentVertices, q - 1, r + 1, "N");
                addIfExists(adjacentVertices, q, r + 1, "N");
                addIfExists(adjacentVertices, q - 1, r + 2, "N");
                break;
            default:
                throw new IllegalArgumentException("Unknown direction: " + direction);
        }

        return adjacentVertices;
    }

    private void addIfExists(List<Vertex> list, int q, int r, String direction) {
        Vertex potentialVertex = getVertex(q, r, direction);
        if (vertices.contains(potentialVertex)) {
            list.add(potentialVertex);
        }
    }


    private boolean isValidRoad(Vertex v1, Vertex v2) {
        return v1.isAdjacentTo(v2);
    }

    private HexType getNextHexType() {
        HexType[] hexTypes = HexType.values();
        HexType selectedType;

        do {
            selectedType = hexTypes[random.nextInt(hexTypes.length)];
        } while (hexTypeCounts.getOrDefault(selectedType, 0) >= hexTypeLimits.getOrDefault(selectedType, 0));

        hexTypeCounts.put(selectedType, hexTypeCounts.getOrDefault(selectedType, 0) + 1);
        return selectedType;
    }

    private HexNumber getNextHexNumber() {
        HexNumber[] hexNumbers = HexNumber.values();
        HexNumber selectedNumber;

        do {
            selectedNumber = hexNumbers[random.nextInt(hexNumbers.length)];
        } while (hexNumberCounts.getOrDefault(selectedNumber, 0) >= hexNumberLimits.getOrDefault(selectedNumber, 0));

        hexNumberCounts.put(selectedNumber, hexNumberCounts.getOrDefault(selectedNumber, 0) + 1);
        return selectedNumber;
    }

    public Vertex getOrCreateVertex(int q, int r, String direction) {
        for (Vertex vertex : vertices) {
            if (vertex.getQ() == q && vertex.getR() == r && vertex.getDirection().equals(direction)) {
                return vertex;
            }
        }
        Vertex newVertex = new Vertex(q, r, direction);
        vertices.add(newVertex);
        return newVertex;
    }

    public List<Hex> getHexes(){
        return hexes;
    }

    public List<Road> getRoads(){
        return roads;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public Vertex getVertex(int q, int r, String direction) {
        for (Vertex vertex : vertices) {
            if (vertex.getQ() == q && vertex.getR() == r && vertex.getDirection().equals(direction)) {
                return vertex;
            }
        }
        return null;
    }
}

