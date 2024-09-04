import {useGame} from "../context/GameContext.jsx";

const PlayersList = ({ gameId }) => {
    const { players } = useGame()

    return (
        <div>
            <ul>
                {players.length === 0 ? (
                    <p>No players found.</p>
                ) : (
                    players.map((player) => (
                        <li key={player.id} className="list-element">
                            <p>{player.user.username}</p>
                        </li>
                    ))
                )}
            </ul>
        </div>
    );
};

export default PlayersList;
