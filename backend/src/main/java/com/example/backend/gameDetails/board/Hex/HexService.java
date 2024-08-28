package com.example.backend.gameDetails.board.Hex;

import com.example.backend.gameDetails.board.Vertex.Vertex;
import com.example.backend.gameDetails.board.Vertex.VertexRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HexService {
    @Autowired
    private VertexRepository vertexRepository;

    @Autowired
    private HexRepository hexRepository;

    @Transactional
    public Hex createHex(int q, int r, HexType type, HexNumber number) {
        Hex hex = new Hex(q, r, type, number);
        initializeHexVertices(hex);
        return hexRepository.save(hex);
    }

    @Transactional
    public void initializeHexVertices(Hex hex) {
        addVertex(hex, hex.getQ(), hex.getR(), "N");
        addVertex(hex, hex.getQ(), hex.getR() - 1, "S");
        addVertex(hex, hex.getQ() - 1, hex.getR() + 1, "N");
        addVertex(hex, hex.getQ(), hex.getR(), "S");
        addVertex(hex, hex.getQ(), hex.getR() + 1, "N");
        addVertex(hex, hex.getQ() + 1, hex.getR() - 1, "S");
    }

    private void addVertex(Hex hex, int q, int r, String direction) {
        Vertex vertex = vertexRepository.findByQAndRAndDirection(q, r, direction)
                .orElseGet(() -> {
                    Vertex newVertex = new Vertex(q, r, direction);
                    vertexRepository.save(newVertex);
                    return newVertex;
                });
        vertex.addHex(hex);
        hex.getVertices().add(vertex);
    }
}
