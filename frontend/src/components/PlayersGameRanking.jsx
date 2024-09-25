import { useGame } from "../context/GameContext.jsx";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBoxes, faDice, faTrophy } from "@fortawesome/free-solid-svg-icons";

const PlayersGameRanking = () => {
    const { players, currentPlayerIndex } = useGame();

    const calculateTotalResources = (resources) => {
        return resources.BRICK + resources.ROCK + resources.WHEAT + resources.WOOD + resources.WOOL;
    };

    if (players.length > 0) {
        return (
            <ul className="game-ranking-list">
                {players.map((player) => (
                    <li key={player.id}>
                        <div>
                            <p>
                                {player.id === currentPlayerIndex && (
                                    <FontAwesomeIcon icon={faDice}/>
                                )}
                                {player.user.username}
                            </p>
                            <p style={{color: calculateTotalResources(player.resources) > 7 ? '#BD1E1E' : 'white'}}>
                                <FontAwesomeIcon icon={faBoxes}/> {calculateTotalResources(player.resources)}
                            </p>
                            <p>
                                <FontAwesomeIcon icon={faTrophy}/> {player.points}
                            </p>
                        </div>
                    </li>
                ))}
            </ul>
        );
    }

    return null;
};

export default PlayersGameRanking;
