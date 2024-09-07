import {useGame} from "../context/GameContext.jsx";
import ResourcePanel from "../components/ResourcePanel.jsx";
import {useEffect} from "react";
import {useParams} from "react-router-dom";
import Board from "../components/Board.jsx";
import PlayersGameRanking from "../components/PlayersGameRanking.jsx";

function GamePage() {
    const { gameId } = useParams()
    const { setGameId, loading } = useGame();

    useEffect(() => {
        setGameId(gameId)
    }, [])

    if (!loading) {
        return (
            <>
                <PlayersGameRanking/>
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
