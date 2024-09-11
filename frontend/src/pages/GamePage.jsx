import {useGame} from "../context/GameContext.jsx";
import ResourcePanel from "../components/ResourcePanel.jsx";
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import Board from "../components/Board.jsx";
import PlayersGameRanking from "../components/PlayersGameRanking.jsx";
import {rollDiceForResources, setNextPLayer} from "../api/authenticatedApi.js";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faArrowRight, faArrowRightArrowLeft, faDice} from "@fortawesome/free-solid-svg-icons";

function GamePage() {
    const { gameId } = useParams()
    const { setGameId, loading, player, currentPlayerIndex, gameRound } = useGame();
    const [diceNumber, setDiceNumber] = useState(() => {
        const savedDiceNumber = localStorage.getItem(`diceRoll_${gameId}`);
        return savedDiceNumber ? parseInt(savedDiceNumber) : 0;
    });

    useEffect(() => {
        setGameId(gameId)
    }, [gameId])

    const rollDice = async () => {
        const die1 = Math.floor(Math.random() * 6) + 1;
        const die2 = Math.floor(Math.random() * 6) + 1;
        const newDiceNumber = die1 + die2
        setDiceNumber(newDiceNumber);
        localStorage.setItem(`diceRoll_${gameId}`, newDiceNumber);
        await rollDiceForResources(gameId, newDiceNumber);
    };

    const nextPlayer = async (diceNumber) => {
        setNextPLayer(gameId, diceNumber)
        setDiceNumber(0);
    }

    const isButtonDisabled = diceNumber !== 0;

    if (!loading) {
        return (
            <>
                <PlayersGameRanking/>
                <Board diceNumber={diceNumber}/>
                <div className="game-buttons-container">
                    <div className="game-buttons">
                        {diceNumber !== 0 && (
                            <button className="next-button" onClick={() => nextPlayer(diceNumber)}><FontAwesomeIcon icon={faArrowRight}/></button>
                        )}
                        {currentPlayerIndex === player.id && gameRound > 1 && (
                            <button className="dice" disabled={isButtonDisabled}
                                    style={{opacity: isButtonDisabled ? 0.5 : 1}} onClick={rollDice}>
                                {diceNumber}
                            </button>
                        )}
                    </div>
                </div>
                <ResourcePanel/>
            </>
        )
    } else {
        return (
            <div>Loading...</div>
        )
    }
}

export default GamePage;
