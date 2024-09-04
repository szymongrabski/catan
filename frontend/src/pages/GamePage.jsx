import {useGame} from "../context/GameContext.jsx";
import ResourcePanel from "../components/ResourcePanel.jsx";
import {useEffect} from "react";
import {useParams} from "react-router-dom";
import Board from "../components/Board.jsx";

function GamePage() {
    const { gameId } = useParams();
    const { setGameId, fetchCurrentPlayerIndex } = useGame();

    useEffect(() => {
        setGameId(gameId);
    }, [gameId])

    return (
        <>
            <h1>Game Page</h1>
            <Board/>
            <ResourcePanel />
        </>
    )
}

export default GamePage;
