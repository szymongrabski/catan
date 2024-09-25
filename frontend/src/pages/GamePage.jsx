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
    const { setGameId, loading, player, currentPlayerIndex, gameRound, diceNumber, isRobberPlaced, robberHex } = useGame();
    const [isRolling, setIsRolling] = useState(false);

    useEffect(() => {
        console.log("robber Hex", robberHex)
        setGameId(gameId)
    }, [gameId])

    const rollDice = async () => {
        setIsRolling(true);

        await new Promise((resolve) => setTimeout(resolve, 1000));

        await rollDiceForResources(gameId);
        setIsRolling(false);
    };

    const nextPlayer = async (diceNumber) => {
        setNextPLayer(gameId, diceNumber)
    }

    const isButtonDisabled = diceNumber !== 0 || isRolling;

    if (!loading) {
        return (
            <>
                <PlayersGameRanking/>
                <Board diceNumber={diceNumber}/>
                <div className="game-buttons-container">
                    <div className="game-buttons">
                        {diceNumber !== 0 && currentPlayerIndex === player.id && (
                            <button className="next-button" onClick={() => nextPlayer(diceNumber)}><FontAwesomeIcon icon={faArrowRight}/></button>
                        )}
                        {currentPlayerIndex === player.id && gameRound > 1 && (
                            <button className={`dice ${isRolling ? 'shaking' : ''}`}
                                    disabled={isButtonDisabled}
                                    style={{opacity: isButtonDisabled ? 0.5 : 1}}
                                    onClick={rollDice}>
                                {isRolling ? '' :  diceNumber || 'Roll'}
                            </button>
                        )}

                        {currentPlayerIndex !== player.id && gameRound > 1 && diceNumber !== 0 && (
                            <button className={`dice`}
                                    disabled={true}>
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
