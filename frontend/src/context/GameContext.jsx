import {createContext, useState, useContext, useEffect} from 'react';
import { fetchData } from "../api/authenticatedApi.js";


const GameContext = createContext();

export const GameProvider = ({ children }) => {
    const [gameId, setGameId] = useState(null);
    const [socket, setSocket] = useState(null);

    const [player, setPlayer] = useState(null);
    const [players, setPlayers] = useState([]);
    const [board, setBoard] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isReady, setIsReady] = useState(false);
    const [currentPlayerIndex, setCurrentPlayerIndex] = useState(0);
    const [settlements, setSettlements] = useState([]);
    const [roads, setRoads] = useState([]);
    const [availableRoads, setAvailableRoads] = useState([]);
    const [gameRound, setGameRound] = useState(0);

    const fetchPlayer = async () => {
        if (!gameId) return;
        try {
            const response = await fetchData(`game/${gameId}/player`);
            setPlayer(response);
        } catch (err) {
            setError('Failed to fetch player data');
        }
    }

    const fetchBoard = async () => {
        if (!gameId) return;
        try {
            const response = await fetchData(`game/${gameId}/board`);
            setBoard(response);
        } catch (err) {
            setError('Failed to fetch board');
        }
    }

    const fetchPlayers = async () => {
        try {
            const players = await fetchData(`game/${gameId}/players`);

            const sortedPlayers = players.sort((a, b) => a.id - b.id);

            setPlayers(sortedPlayers);
        } catch (error) {
            setError('Failed to fetch players data');
        }
    };

    const fetchCurrentPlayerIndex = async () => {
        try {
            const response = await fetchData(`game/${gameId}/current-player`);
            setCurrentPlayerIndex(response);
        } catch (err) {
            setError('Failed to fetch currentPlayer data');
        }
    }

    const fetchSettlements = async (gameId) => {
        const response = await fetchData(`game/${gameId}/settlements`);
        setSettlements(response);
    }

    const fetchRoads = async (gameId) => {
        const response = await fetchData(`game/${gameId}/roads`);
        setRoads(response);
    }

    const fetchAvailableRoads = async () => {
        const response = await fetchData(`game/${gameId}/${player.id}/available-roads`);
        setAvailableRoads(response);
    }

    const fetchGameRound = async () => {
        const response = await fetchData(`game/${gameId}/round`);
        setGameRound(response);
    }

    const isPlayersTurn = () => {
        return player.id === currentPlayerIndex
    }

    const fetchGameData = async () => {
        if (!gameId) return;
        try {
            await Promise.all([
                fetchPlayer(),
                fetchPlayers(gameId),
                fetchBoard(gameId),
                fetchBoard(gameId),
                fetchSettlements(gameId),
                fetchRoads(gameId),
                fetchGameRound(),
                fetchCurrentPlayerIndex()
            ]);
        } catch (err) {
            setError('Failed to fetch game data');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (gameId) {
            fetchGameData();
        }
    }, [gameId]);

    useEffect(() => {
        if (player) {
            const ws = new WebSocket(`ws://localhost:8080/ws/game?player-id=${player.id}`);

            ws.onopen = () => {
                console.log("Game WebSocket connection opened");
            };

            ws.onmessage = (event) => {
                if (event.data === 'fetch-players') {
                    fetchPlayers(gameId);
                } else if (event.data === 'redirect') {
                    setIsReady(true);
                } else if (event.data === 'fetch-settlements') {
                    fetchSettlements(gameId);
                } else if (event.data === 'fetch-roads') {
                    fetchRoads(gameId);
                } else if (event.data === 'fetch-available-roads') {
                    fetchAvailableRoads(gameId);
                } else if (event.data === 'fetch-current-player-index') {
                    fetchCurrentPlayerIndex();
                    localStorage.setItem(`diceRoll_${gameId}`, 0);
                } else if (event.data === 'fetch-game-round') {
                    fetchGameRound();
                }
            };

            ws.onclose = () => {
                console.log("Game WebSocket connection closed");
            };

            setSocket(ws);

            return () => {
                ws.close();
            };
        }
    }, [player]);

    return (
        <GameContext.Provider value={{gameId, socket, setGameId, player, players, loading, error, board, setError, isReady, fetchPlayer, setIsReady, fetchBoard, currentPlayerIndex, fetchCurrentPlayerIndex, isPlayersTurn, settlements, roads, availableRoads, fetchAvailableRoads, gameRound}}>
            { children }
        </GameContext.Provider>
    );
};

export const useGame = () => useContext(GameContext);
