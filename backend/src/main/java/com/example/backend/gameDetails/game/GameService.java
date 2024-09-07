package com.example.backend.gameDetails.game;

import com.example.backend.gameDetails.board.Board;
import com.example.backend.gameDetails.board.Road.Road;
import com.example.backend.gameDetails.board.Vertex.Vertex;
import com.example.backend.gameDetails.player.Player;
import com.example.backend.gameDetails.player.PlayerDTO;
import com.example.backend.gameDetails.player.PlayerRepository;
import com.example.backend.gameDetails.player.PlayerRole;
import com.example.backend.user.User;
import com.example.backend.user.UserDTO;
import com.example.backend.user.UserService;
import com.example.backend.websocket.GameWebSocketHandler;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final List<Game> games = new ArrayList<>();

    private final PlayerRepository playerRepository;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    private final GameWebSocketHandler gameWebSocketHandler;

    public GameService(PlayerRepository playerRepository, UserService userService, GameWebSocketHandler gameWebSocketHandler) {
        this.playerRepository = playerRepository;
        this.userService = userService;
        this.gameWebSocketHandler = gameWebSocketHandler;

    }

    public List<Game> getAllGames() {
        return games;
    }

    public Optional<Game> getGameById(Long id) {
        return games.stream()
                .filter(game -> game.getId().equals(id))
                .findFirst();
    }

    public void addGame(Game game) {
        games.add(game);
    }

    @Transactional
    public Game createGame() {
        Game game = new Game();
        Board board = new Board();
        game.setBoard(board);
        User currentUser = userService.getCurrentUser();

        addGame(game);

        addPlayerToGame(game.getId(), currentUser, PlayerRole.ADMIN);

        return game;
    }

    @Transactional
    public void addPlayerToGame(Long gameId, User user, PlayerRole role) {
        Player player = new Player();
        player.setUser(user);
        player.setGameId(gameId);
        player.setRole(role);
        playerRepository.save(player);

        Optional<Game> gameOptional = getGameById(gameId);
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            game.addPlayer(player);
        }

        gameWebSocketHandler.notifyUserAboutFetchingPlayers();
    }

    @Transactional
    public void startGame(Long gameId) {
        Optional<Game> gameOptional = getGameById(gameId);

        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();

            try {
                game.startGame();
            } catch (IllegalStateException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }

            gameWebSocketHandler.notifyAboutRedirection();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with ID " + gameId + " not found");
        }
    }

    public List<PlayerDTO> getPlayersForGame(Long gameId) {
        Optional<Game> gameOptional = getGameById(gameId);

        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            return game.getPlayers().stream()
                    .map(this::convertToPlayerDTO)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Game with ID " + gameId + " not found");
        }
    }

    public PlayerDTO getPlayer(Long gameId) {
        Optional<Game> gameOptional = getGameById(gameId);
        User currentUser = userService.getCurrentUser();

        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            PlayerDTO playerDTO = game.getPlayers().stream()
                    .filter(player -> player.getUser().getId().equals(currentUser.getId()))
                    .map(this::convertToPlayerDTO)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Player not found in the game"));

            logger.info(playerDTO.getResources().toString());
            return playerDTO;
        } else {
            throw new IllegalArgumentException("Game with ID " + gameId + " not found");
        }
    }

    private PlayerDTO convertToPlayerDTO(Player player) {
        UserDTO userDTO = userService.convertToUserDTO(player.getUser());
        return new PlayerDTO(player.getId(), player.getPoints(), player.getRole(), userDTO, player.getResources());
    }

    public Long getCurrentPlayerIndex(Long gameId) {
        Optional<Game> gameOptional = getGameById(gameId);
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            return game.getCurrentPlayerIndex();
        } else {
            throw new IllegalArgumentException("Game with ID " + gameId + " not found");
        }
    }

    public List<Vertex> getAvailableVertices(Long gameId) {
        Optional<Game> gameOptional = getGameById(gameId);
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            Board board = game.getBoard();

            if (board == null) {
                throw new IllegalStateException("Board not initialized for the game with ID " + gameId);
            }

            if (game.getRoundNumber() <= 2) {
                return board.getVertices().stream()
                        .filter(Vertex::isBuildable)
                        .collect(Collectors.toList());
            }

            // add logic for next game stage
            return board.getVertices();
        } else {
            throw new IllegalArgumentException("Game with ID " + gameId + " not found");
        }
    }

    public void placeSettlement(Long gameId, Long playerId, int q, int r, String direction) {
        Game game = getGameById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game with ID " + gameId + " not found"));

        Board board = game.getBoard();

        Player player = playerRepository.findByIdAndGameId(playerId, gameId)
                .orElseThrow(() -> new IllegalArgumentException("Player with ID " + playerId + " not found"));

        Vertex vertex = board.getVertex(q, r, direction);

        if (vertex.isOccupied()) {
            throw new IllegalStateException("Vertex is occupied");
        }

        if (!vertex.isBuildable()) {
            throw new IllegalStateException("Vertex is not buildable");
        }

        List<Vertex> adjacentVertices = board.getAdjacentVertices(vertex);
        for (Vertex adjacentVertex : adjacentVertices) {
            adjacentVertex.setBuildable(false);
        }

        vertex.setOwnerId(player.getId());
        vertex.setBuildable(false);

        player.addPoints(1);
        playerRepository.save(player);

        gameWebSocketHandler.notifyAboutFetchingSettlements();
        gameWebSocketHandler.notifyAboutFetchingAvailableRoads();
    }

    public void placeRoad(Long gameId, Long playerId, Road road) {
        Game game = getGameById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game with ID " + gameId + " not found"));

        Board board = game.getBoard();

        Player player = playerRepository.findByIdAndGameId(playerId, gameId)
                .orElseThrow(() -> new IllegalArgumentException("Player with ID " + playerId + " not found"));

        Road road1 = board.getRoad(road.getStartVertex(), road.getEndVertex());
        logger.info(road1.toString());

        if (road1 == null) {
            throw new IllegalStateException("Road not found");
        }

        if (road1.isOccupied()) {
            throw new IllegalStateException("Road is occupied");
        }

        road1.setOwnerId(player.getId());
        playerRepository.save(player);
        logger.info(road1.toString());
        // add logic to count longest road for player
        gameWebSocketHandler.notifyAboutFetchingRoads();
    }

    public List<Vertex> getSettlementVertices(Long gameId) {
        Game game = getGameById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game with ID " + gameId + " not found"));

        Board board = game.getBoard();
        List<Vertex> vertices = board.getVertices().stream()
                .filter(Vertex::isOccupied)
                .toList();

        logger.info(vertices.toString());

        return vertices;
    }

    public List<Road> getAvailableRoads(Long gameId, Long playerId) {
        Game game = getGameById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game with ID " + gameId + " not found"));

        Board board = game.getBoard();

        Player player = playerRepository.findByIdAndGameId(playerId, gameId)
                .orElseThrow(() -> new IllegalArgumentException("Player with ID " + playerId + " not found"));

        List<Vertex> playerVertices = board.getVertices().stream()
                .filter(vertex -> vertex.getOwnerId() != null && vertex.getOwnerId().equals(playerId))
                .collect(Collectors.toList());

        List<Road> playerRoads = board.getRoads().stream()
                .filter(road -> road.getOwnerId() != null && road.getOwnerId().equals(playerId))
                .toList();

        List<Road> availableRoads = board.getRoads().stream()
                .filter(road -> isRoadAdjacentToPlayerVertex(road, playerVertices) || isRoadAdjacentToPlayerRoad(road, playerRoads))
                .filter(road -> !road.isOccupied())
                .collect(Collectors.toList());

        return availableRoads;
    }

    public List<Road> getRoads(Long gameId) {
        Game game = getGameById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game with ID " + gameId + " not found"));

        Board board = game.getBoard();

        return board.getRoads().stream()
                .filter(Road::isOccupied)
                .collect(Collectors.toList());
    }


    private boolean isRoadAdjacentToPlayerVertex(Road road, List<Vertex> playerVertices) {
        return playerVertices.contains(road.getStartVertex()) || playerVertices.contains(road.getEndVertex());
    }

    private boolean isRoadAdjacentToPlayerRoad(Road road, List<Road> playerRoads) {
        for (Road playerRoad : playerRoads) {
            if (road.getStartVertex().equals(playerRoad.getStartVertex()) ||
                    road.getStartVertex().equals(playerRoad.getEndVertex()) ||
                    road.getEndVertex().equals(playerRoad.getStartVertex()) ||
                    road.getEndVertex().equals(playerRoad.getEndVertex())) {
                return true;
            }
        }
        return false;
    }
}

