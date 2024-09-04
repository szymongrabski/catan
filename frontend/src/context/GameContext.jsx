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

    const fetchPlayer = async () => {
        if (!gameId) return;

        try {
            const response = await fetchData(`game/${gameId}/player`);
            setPlayer(response);
        } catch (err) {
            setError('Failed to fetch player data');
        } finally {
            setLoading(false);
        }
    }

    const fetchBoard = async () => {
        if (!gameId) return;
        try {
            console.log("fetching board")
            const response = await fetchData(`game/${gameId}/board`);
            setBoard(response);
        } catch (err) {
            setError('Failed to fetch board');
        } finally {
            setLoading(false);
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
            const response = await fetchData(`game/${gameId}/currentPlayer`);
            console.log("fetching current player index " + response)
            setCurrentPlayerIndex(response.currentPlayerIndex);
        } catch (err) {
            setError('Failed to fetch currentPlayer data');
        }
    }

    useEffect(() => {
        if (gameId) {
            fetchPlayer();
            fetchPlayers();
            fetchBoard();
            fetchCurrentPlayerIndex();

            const ws = new WebSocket(`ws://localhost:8080/ws/game?game-id=${gameId}`);
            ws.onopen = () => {
                console.log("Game WebSocket connection opened");
            };

            ws.onmessage = (event) => {
                if (event.data === 'fetch-players') {
                    fetchPlayers();
                } else if (event.data === 'redirect') {
                    setIsReady(true);
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
    }, [gameId])

    return (
        <GameContext.Provider value={{gameId, socket, setGameId, player, players, loading, error, board, setError, isReady, fetchPlayer, setIsReady, fetchBoard, currentPlayerIndex, fetchCurrentPlayerIndex}}>
            { children }
        </GameContext.Provider>
    );
};

export const useGame = () => useContext(GameContext);
