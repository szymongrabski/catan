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
            setPlayers(players);
        } catch (error) {
            setError('Failed to fetch players data');
        }
    };

    const fetchCurrentPlayerIndex = async () => {
        try {
            const response = await fetchData(`game/${gameId}/current-player`);
            console.log("fetching currentPlayerIndex", response);
            setCurrentPlayerIndex(response);
        } catch (err) {
            setError('Failed to fetch currentPlayer data');
        }
    }

    const fetchSettlements = async (gameId) => {
        const response = await fetchData(`game/${gameId}/settlements`);
        setSettlements(response);
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
                fetchSettlements(gameId)
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
                    console.log("REDIRECTING");
                    setIsReady(true);
                } else if (event.data === 'fetch-settlements') {
                    fetchSettlements(gameId);
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
        <GameContext.Provider value={{gameId, socket, setGameId, player, players, loading, error, board, setError, isReady, fetchPlayer, setIsReady, fetchBoard, currentPlayerIndex, fetchCurrentPlayerIndex, isPlayersTurn, settlements}}>
            { children }
        </GameContext.Provider>
    );
};

export const useGame = () => useContext(GameContext);
