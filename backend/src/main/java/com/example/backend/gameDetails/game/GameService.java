package com.example.backend.gameDetails.game;


import com.example.backend.gameDetails.board.Board;
import com.example.backend.gameDetails.board.BoardRepository;
import com.example.backend.gameDetails.board.BoardService;
import com.example.backend.gameDetails.player.Player;
import com.example.backend.gameDetails.player.PlayerDTO;
import com.example.backend.gameDetails.player.PlayerRepository;
import com.example.backend.gameDetails.player.PlayerRole;
import com.example.backend.user.User;
import com.example.backend.user.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final UserService userService;
    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);


    public GameService(GameRepository gameRepository, PlayerRepository playerRepository, UserService userService, BoardService boardService, BoardRepository boardRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.userService = userService;
        this.boardService = boardService;
        this.boardRepository = boardRepository;

    }

    public Game createGame() {
        Game game = new Game();
        Board board = boardService.createBoard();
        game.setBoard(board);
        board.setGame(game);

        gameRepository.save(game);

        logger.info("Z game service");
        logger.info(game.getBoard().getHexes().toString());

        User currentUser = userService.getCurrentUser();
        addPlayerToGame(game, currentUser, PlayerRole.ADMIN);

        return game;
    }
    public void addPlayerToGame(Game game, User user, PlayerRole role) {
        if (playerRepository.existsByGameAndUser(game, user)) {
            throw new IllegalArgumentException("User is already in the game");
        }

        Player player = new Player();
        player.setUser(user);
        player.setGame(game);
        player.setRole(role);
        playerRepository.save(player);
    }

    public List<PlayerDTO> getPlayersForGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid game ID"));
        List<Player> players = playerRepository.findByGame(game);
        return players.stream()
                .map(player -> new PlayerDTO(player.getId(), player.getPoints(), player.getRole(), userService.convertToUserDTO(player.getUser())))
                .collect(Collectors.toList());
    }

    public PlayerDTO getPlayer(Long gameId) {
        User user = userService.getCurrentUser();

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid game ID"));

        Player player = playerRepository.findByGameAndUser(game, user)
                .orElseThrow(() -> new IllegalArgumentException("Invalid player ID"));

        return new PlayerDTO(player.getId(), player.getPoints(), player.getRole(), userService.convertToUserDTO(player.getUser()));
    }

    @Transactional
    public Board getBoard(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid game ID"));

        logger.info(game.getBoard().getHexes().toString());
        return game.getBoard();
    }
}
