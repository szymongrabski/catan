package com.example.backend.gameDetails.game;

import com.example.backend.gameDetails.board.Board;
import com.example.backend.gameDetails.board.Road.Road;
import com.example.backend.gameDetails.board.Vertex.Vertex;
import com.example.backend.gameDetails.player.PlayerDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createGame() {
        Game game = gameService.createGame();
        return ResponseEntity.ok(game.getId());
    }

    @PostMapping("/{gameId}/start")
    public ResponseEntity<Map<String, Object>> startGame(@PathVariable Long gameId) {
        try {
            gameService.startGame(gameId);
            Long currentPlayerIndex = gameService.getCurrentPlayerIndex(gameId);

            Map<String, Object> response = new HashMap<>();
            response.put("currentPlayerIndex", currentPlayerIndex);

            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{gameId}/current-player")
    public ResponseEntity<Long> getCurrentPlayer(@PathVariable Long gameId) {
        try {
           Long currentPlayerIndex = gameService.getCurrentPlayerIndex(gameId);
            return ResponseEntity.ok(currentPlayerIndex);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }
    }

    @GetMapping("/{gameId}/players")
    public List<PlayerDTO> getPlayersForGame(@PathVariable Long gameId) {
        return gameService.getPlayersForGame(gameId);
    }

    @GetMapping("/{gameId}/player")
    public PlayerDTO getPlayerForGame(@PathVariable Long gameId) {
        return gameService.getPlayer(gameId);
    }

    @GetMapping("/{gameId}/board")
    public ResponseEntity<Board> getGameBoard(@PathVariable Long gameId) {
        Optional<Game> game = gameService.getGameById(gameId);

        return game.map(value -> ResponseEntity.ok(value.getBoard())).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{gameId}/available-vertices")
    public ResponseEntity<List<Vertex>> getAvailableVertices(@PathVariable Long gameId) {
        try {
            List<Vertex> availableVertices = gameService.getAvailableVertices(gameId);
            return ResponseEntity.ok(availableVertices);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/{gameId}/settlements")
    public ResponseEntity<String> placeSettlement(
            @PathVariable Long gameId,
            @RequestParam Long playerId,
            @RequestParam int q,
            @RequestParam int r,
            @RequestParam String direction) {
        try {
            gameService.placeSettlement(gameId, playerId, q, r, direction);
            return ResponseEntity.ok("Settlement placed successfully");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{gameId}/settlements")
    public ResponseEntity<List<Vertex>> getSettlements(@PathVariable Long gameId) {
        try {
            List<Vertex> settlements = gameService.getSettlementVertices(gameId);
            return ResponseEntity.ok(settlements);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{gameId}/{playerId}/available-roads")
    public ResponseEntity<List<Road>> getAvailableRoads(@PathVariable Long gameId, @PathVariable Long playerId) {
        try {
            List<Road> settlements = gameService.getAvailableRoads(gameId, playerId);
            return ResponseEntity.ok(settlements);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
