package com.example.backend.gameDetails.game;

import com.example.backend.gameDetails.board.Board;
import com.example.backend.gameDetails.player.PlayerDTO;
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
            int currentPlayerIndex = gameService.getCurrentPlayerIndex(gameId);

            Map<String, Object> response = new HashMap<>();
            response.put("currentPlayerIndex", currentPlayerIndex);

            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{gameId}/currentPlayer")
    public ResponseEntity<Integer> getCurrentPlayer(@PathVariable Long gameId) {
        try {
            Integer currentPlayerIndex = gameService.getCurrentPlayerIndex(gameId);
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

    @PostMapping("/{gameId}/place-settlement")
    public ResponseEntity<Void> placeSettlement(@PathVariable Long gameId, @RequestBody Long playerId, @RequestBody int q, @RequestBody int r) {
        try {
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
