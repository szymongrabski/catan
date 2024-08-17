package com.example.backend.gameDetails.game;

import com.example.backend.gameDetails.player.PlayerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{gameId}/players")
    public List<PlayerDTO> getPlayersForGame(@PathVariable Long gameId) {
        return gameService.getPlayersForGame(gameId);
    }
}
