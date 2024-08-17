package com.example.backend.gameDetails.game;


import com.example.backend.gameDetails.player.Player;
import com.example.backend.gameDetails.player.PlayerDTO;
import com.example.backend.gameDetails.player.PlayerRepository;
import com.example.backend.user.User;
import com.example.backend.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final UserService userService;

    public GameService(GameRepository gameRepository, PlayerRepository playerRepository, UserService userService) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.userService = userService;
    }

    public Game createGame() {
        Game game = new Game();
        gameRepository.save(game);

        User currentUser = userService.getCurrentUser();
        addPlayerToGame(game, currentUser);
        return game;
    }

    public void addPlayerToGame(Game game, User user) {
        Player player = new Player();
        player.setUser(user);
        player.setGame(game);
        playerRepository.save(player);
    }

    public List<PlayerDTO> getPlayersForGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid game ID"));
        List<Player> players = playerRepository.findByGame(game);
        players.forEach(player -> System.out.println("Player ID: " + player.getId()));
        return players.stream()
                .map(player -> new PlayerDTO(player.getId(), player.getPoints(), userService.convertToUserDTO(player.getUser())))
                .collect(Collectors.toList());
    }


}
