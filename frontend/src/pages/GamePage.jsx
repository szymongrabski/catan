import {useGame} from "../context/GameContext.jsx";
import ResourcePanel from "../components/ResourcePanel.jsx";
import {useEffect} from "react";
import {useParams} from "react-router-dom";
import Board from "../components/Board.jsx";

function GamePage() {
    const { gameId } = useParams()
    const { setGameId, player, currentPlayerIndex, loading } = useGame();

    useEffect(() => {
        setGameId(gameId)
    }, [])

    if (!loading) {
        return (
            <>
                <h1>Game Page</h1>
                {player.id === currentPlayerIndex && (
                    <p>Your Turn</p>
                )}
                <Board/>
                <ResourcePanel />
            </>
        )
    } else {
        return (
            <div>Loading...</div>
        )
    }
}

export default GamePage;
