package com.example.backend.gameDetails.game;

import com.example.backend.gameDetails.board.Board;
import com.example.backend.gameDetails.board.Hex.HexData;
import com.example.backend.gameDetails.board.Hex.HexType;
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

import java.util.*;
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
            gameWebSocketHandler.notifyAboutFetchingCurrentPlayerIndex();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with ID " + gameId + " not found");
        }
    }


    public List<PlayerDTO> getPlayersForGame(Long gameId) {
        List<Player> players = playerRepository.findByGameId(gameId);

        if (players.isEmpty()) {
            throw new IllegalArgumentException("No players found for game ID " + gameId);
        }

        return players.stream()
                .map(this::convertToPlayerDTO)
                .collect(Collectors.toList());
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

    public List<Vertex> getAvailableVertices(Long gameId, Long playerId) {
        Optional<Game> gameOptional = getGameById(gameId);
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            Board board = game.getBoard();

            if (board == null) {
                throw new IllegalStateException("Board not initialized for the game with ID " + gameId);
            }

            Player player = playerRepository.findByIdAndGameId(playerId, gameId)
                    .orElseThrow(() -> new IllegalArgumentException("Player with ID " + playerId + " not found"));

            boolean hasEnoughResources = player.getResources().getOrDefault(HexType.WOOD, 0) >= 1 &&
                    player.getResources().getOrDefault(HexType.BRICK, 0) >= 1 && player.getResources().getOrDefault(HexType.WOOL, 0) >= 1
                    && player.getResources().getOrDefault(HexType.WHEAT, 0) >= 1;

            if (game.getRoundNumber() <= 1) {
                // change to show roads from only the last placed settlement
                return board.getVertices().stream()
                        .filter(Vertex::isBuildable)
                        .collect(Collectors.toList());
            } else if (hasEnoughResources) {
                List<Road> playerRoads = board.getRoads().stream()
                        .filter(road -> road.getOwnerId() != null && road.getOwnerId().equals(playerId))
                        .collect(Collectors.toList());

                return board.getVertices().stream()
                        .filter(vertex -> vertex.isBuildable() && isVertexAdjacentToPlayerRoad(vertex, playerRoads))
                        .toList();
            }
            return new ArrayList<>();
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

        boolean hasEnoughResources = player.getResources().getOrDefault(HexType.WOOD, 0) >= 1 &&
                player.getResources().getOrDefault(HexType.BRICK, 0) >= 1 && player.getResources().getOrDefault(HexType.WOOL, 0) >= 1
                && player.getResources().getOrDefault(HexType.WHEAT, 0) >= 1;

        if (game.getRoundNumber() > 1 && !hasEnoughResources) {
            throw new IllegalStateException("Not enough resources");
        }

        List<Vertex> adjacentVertices = board.getAdjacentVertices(vertex);
        for (Vertex adjacentVertex : adjacentVertices) {
            adjacentVertex.setBuildable(false);
        }

        vertex.setOwnerId(player.getId());
        vertex.setBuildable(false);

        if (game.getRoundNumber() > 1) {
            Integer wood = player.getResources().getOrDefault(HexType.WOOD, 0);
            Integer brick = player.getResources().getOrDefault(HexType.BRICK, 0);
            Integer wool = player.getResources().getOrDefault(HexType.WOOL, 0);
            Integer wheat = player.getResources().getOrDefault(HexType.WHEAT, 0);;
            player.getResources().put(HexType.WOOD, wood - 1);
            player.getResources().put(HexType.BRICK, brick - 1);
            player.getResources().put(HexType.WOOL, wool - 1);
            player.getResources().put(HexType.WHEAT, wheat - 1);
        }

        player.addPoints(1);

        playerRepository.save(player);

        if (game.getRoundNumber() == 1) {
            addResources(player, vertex);
        }

        gameWebSocketHandler.notifyUserAboutFetchingPlayers();
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

        if (road1 == null) {
            throw new IllegalStateException("Road not found");
        }

        if (road1.isOccupied()) {
            throw new IllegalStateException("Road is occupied");
        }

        if (game.getRoundNumber() > 1) {
            Integer wood = player.getResources().getOrDefault(HexType.WOOD, 0);
            Integer brick = player.getResources().getOrDefault(HexType.BRICK, 0);

            if (wood < 1 || brick < 1) {
                throw new IllegalStateException("Not enough resources to build a road");
            }

            player.getResources().put(HexType.WOOD, wood - 1);
            player.getResources().put(HexType.BRICK, brick - 1);
        }

        road1.setOwnerId(player.getId());
        playerRepository.save(player);

        // add logic to count longest road for player
        gameWebSocketHandler.notifyAboutFetchingRoads();
        gameWebSocketHandler.notifyUserAboutFetchingPlayers();
        if (game.getRoundNumber() <= 1) {
            nextPlayer(gameId);
            gameWebSocketHandler.notifyAboutFetchingCurrentPlayerIndex();
        }
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

        List<Road> availableRoads = new ArrayList<>();

        boolean hasEnoughResources = player.getResources().getOrDefault(HexType.WOOD, 0) >= 1 &&
                player.getResources().getOrDefault(HexType.BRICK, 0) >= 1;

        if (game.getRoundNumber() <= 1) {
            availableRoads = board.getRoads().stream()
                    .filter(road -> isRoadAdjacentToPlayerVertex(road, playerVertices))
                    .filter(road -> !road.isOccupied())
                    .collect(Collectors.toList());
        } else if (hasEnoughResources){
            List<Road> playerRoads = board.getRoads().stream()
                    .filter(road -> road.getOwnerId() != null && road.getOwnerId().equals(playerId))
                    .toList();

           availableRoads = board.getRoads().stream()
                    .filter(road -> isRoadAdjacentToPlayerVertex(road, playerVertices) || isRoadAdjacentToPlayerRoad(road, playerRoads))
                    .filter(road -> !road.isOccupied())
                    .collect(Collectors.toList());
        }

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

    private boolean isVertexAdjacentToPlayerRoad(Vertex vertex, List<Road> playerRoads) {
        return playerRoads.stream()
                .anyMatch(road -> road.getStartVertex().equals(vertex) || road.getEndVertex().equals(vertex));
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

    public void addResources(Player player, Vertex vertex) {
        for (HexData hexData : vertex.getHexDataList()) {
            HexType hexType = hexData.getType();
            player.getResources().merge(hexType, 1, Integer::sum);
        }
        playerRepository.save(player);
    }

    public void nextPlayer(Long gameId) {
        Game game = getGameById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game with ID " + gameId + " not found"));

        List<Player> players = game.getPlayers();
        if (players.isEmpty()) {
            throw new IllegalStateException("No players in the game.");
        }

        int currentIndex = game.getCurrentIndex();
        int startRoundIndex = game.getStartRoundIndex();

        currentIndex = (currentIndex + 1) % players.size();
        game.setCurrentIndex(currentIndex);

        if (currentIndex == startRoundIndex) {
            game.addToRoundNumber();
            gameWebSocketHandler.notifyAboutFetchingGameRound();
        }

        game.setDiceNumber(0);

        gameWebSocketHandler.notifyAboutFetchingDiceNumber();
        gameWebSocketHandler.notifyAboutFetchingCurrentPlayerIndex();
    }


    public void addResourcesAfterDiceRoll(Long gameId) {
        Game game = getGameById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game with ID " + gameId + " not found"));
        List<Player> players = playerRepository.findByGameId(gameId);

        int diceRoll = game.getDiceNumber();

        Board board = game.getBoard();
        if (board == null) {
            throw new IllegalStateException("Board not initialized for game with ID " + gameId);
        }

        List<Vertex> verticesWithRolledNumber = board.getVertices().stream()
                .filter(vertex -> vertex.getHexDataList().stream()
                        .anyMatch(hexData -> hexData.getNumber().getValue() == diceRoll))
                .toList();

        verticesWithRolledNumber.forEach(vertex -> {
            Player player = players.stream()
                    .filter(p -> p.getId().equals(vertex.getOwnerId()))
                    .findFirst()
                    .orElse(null);

            if (player != null) {
                vertex.getHexDataList().stream()
                        .filter(hexData -> hexData.getNumber().getValue() == diceRoll)
                        .forEach(hexData -> {
                            int resourceAmount = vertex.isUpgraded() ? 2 : 1;
                            player.getResources().merge(hexData.getType(), resourceAmount, Integer::sum);
                        });

                playerRepository.save(player);
            }
        });

        gameWebSocketHandler.notifyUserAboutFetchingPlayers();
    }

    public void substarctResources(Long gameId, Long playerId, Map<HexType, Integer> resourcesToRemove) {
        Game game = getGameById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game with ID " + gameId + " not found"));

        int diceRoll = game.getDiceNumber();

        Board board = game.getBoard();
        if (board == null) {
            throw new IllegalStateException("Board not initialized for game with ID " + gameId);
        }

        Player player = playerRepository.findByIdAndGameId(playerId, gameId)
                .orElseThrow(() -> new IllegalArgumentException("Player with ID " + playerId + " not found"));

        resourcesToRemove.forEach((resourceType, amountToRemove) -> {
            Integer currentAmount = player.getResources().getOrDefault(resourceType, 0);

            if (currentAmount < amountToRemove) {
                throw new IllegalArgumentException("Not enough resources for player with ID " + playerId + " to remove " + amountToRemove + " of " + resourceType);
            }
            player.getResources().merge(resourceType, -amountToRemove, Integer::sum);
        });

        playerRepository.save(player);

        gameWebSocketHandler.notifyUserAboutFetchingPlayers();
    }

    public void upgradeSettlement(Long gameId, Long playerId, int q, int r, String direction) {
        Game game = getGameById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game with ID " + gameId + " not found"));

        Player player = playerRepository.findByIdAndGameId(playerId, gameId)
                .orElseThrow(() -> new IllegalArgumentException("Player with ID " + playerId + " not found"));

        Vertex vertex = game.getBoard().getVertex(q, r, direction);

        if (!Objects.equals(vertex.getOwnerId(), player.getId())) {
            throw new IllegalStateException("Player with ID " + playerId + " is not owner of the vertex");
        }

        Integer wheat = player.getResources().getOrDefault(HexType.WHEAT, 0);
        Integer rock = player.getResources().getOrDefault(HexType.ROCK, 0);

        if (wheat < 2 || rock < 3) {
            throw new IllegalStateException("Not enough resources to upgrade settlement");
        }

        player.getResources().put(HexType.WHEAT, wheat  - 2);
        player.getResources().put(HexType.ROCK, rock - 3);
        player.addPoints(1);

        vertex.setUpgraded(true);

        playerRepository.save(player);

        gameWebSocketHandler.notifyAboutFetchingSettlements();
        gameWebSocketHandler.notifyUserAboutFetchingPlayers();
    }

    public void rollDice(Long gameId) {
        Game game = getGameById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game with ID " + gameId + " not found"));

        int number1 = (int) (Math.random() * 6) + 1;
        int number2 = (int) (Math.random() * 6) + 1;

        game.setDiceNumber(number1 + number2);

        gameWebSocketHandler.notifyAboutFetchingDiceNumber();
    }

    public int getDiceNumber(Long gameId) {
        Game game = getGameById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game with ID " + gameId + " not found"));
        return game.getDiceNumber();
    }
}



