package com.example.backend.gameDetails.board.Vertex;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VertexRepository extends JpaRepository<Vertex, Long> {
    Optional<Vertex> findByQAndRAndDirection(int q, int r, String direction);
}
