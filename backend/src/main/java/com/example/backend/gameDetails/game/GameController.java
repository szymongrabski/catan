package com.example.backend.gameDetails.game;

import com.example.backend.gameDetails.board.Board;
import com.example.backend.gameDetails.board.Hex.HexType;
import com.example.backend.gameDetails.board.Road.Road;
import com.example.backend.gameDetails.board.Vertex.Vertex;
import com.example.backend.gameDetails.board.Vertex.VertexRequest;
import com.example.backend.gameDetails.player.PlayerDTO;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/game")
public class GameController {
    private final GameService gameService;
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

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

    @GetMapping("/{gameId}/{playerId}/available-vertices")
    public ResponseEntity<List<Vertex>> getAvailableVertices(@PathVariable Long gameId, @PathVariable Long playerId) {
        try {
            List<Vertex> availableVertices = gameService.getAvailableVertices(gameId, playerId);
            return ResponseEntity.ok(availableVertices);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{gameId}/round")
    public ResponseEntity<Integer> getGameRound(@PathVariable Long gameId) {
        Optional<Game> game = gameService.getGameById(gameId);

        if (game.isPresent()) {
            return ResponseEntity.ok(game.get().getRoundNumber());
        } else {
            return ResponseEntity.notFound().build();
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

    @GetMapping("/{gameId}/roads")
    public ResponseEntity<List<Road>> getRoads(@PathVariable Long gameId) {
        try {
            List<Road> roads = gameService.getRoads(gameId);
            return ResponseEntity.ok(roads);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{gameId}/{playerId}/roads")
    public ResponseEntity<String> placeRoad(
            @PathVariable Long gameId,
            @PathVariable Long playerId,
            @RequestBody Road road) {
        try {
            gameService.placeRoad(gameId, playerId, road);
            return ResponseEntity.ok("Road placed successfully");
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.info(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{gameId}/dice")
    public ResponseEntity<Integer> getDiceNumber(@PathVariable Long gameId) {
        int diceNumber = gameService.getDiceNumber(gameId);
        return ResponseEntity.ok(diceNumber);
    }

    @PostMapping("{gameId}/dice")
    public ResponseEntity<String> rollDice(@PathVariable Long gameId) {
        gameService.rollDice(gameId);
        gameService.addResourcesAfterDiceRoll(gameId);
        return ResponseEntity.ok("Resources updated after dice roll.");
    }

    @PostMapping("{gameId}/next")
    public ResponseEntity<String> setNextPlayer(@PathVariable Long gameId) {
        gameService.nextPlayer(gameId);
        return ResponseEntity.ok("Next round successfully");
    }

    @PostMapping("{gameId}/{playerId}/settlements/upgrade")
    public ResponseEntity<String> upgradeSettlement(@PathVariable Long gameId, @PathVariable Long playerId, @RequestBody VertexRequest vertexRequest) {
        gameService.upgradeSettlement(gameId, playerId, vertexRequest.getQ(), vertexRequest.getR(), vertexRequest.getDirection());
        return ResponseEntity.ok("Settlement upgraded successfully");
    }

    @PatchMapping("/{gameId}/{playerId}/resources")
    public ResponseEntity<Void> removePlayerResources(@PathVariable Long gameId,
                                                      @PathVariable Long playerId,
                                                      @RequestBody Map<String, Integer> resourcesToRemove) {
        try {
            Map<HexType, Integer> hexResources = resourcesToRemove.entrySet().stream()
                    .collect(Collectors.toMap(
                            entry -> HexType.valueOf(entry.getKey().toUpperCase()),
                            entry -> entry.getValue()
                    ));

            gameService.substarctResources(gameId, playerId, hexResources);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
