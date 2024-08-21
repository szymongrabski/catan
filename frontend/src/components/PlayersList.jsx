import {useEffect, useState} from "react";
import { fetchData } from "../api/authenticatedApi.js";

const PlayersList = ({ gameId }) => {
    const [players, setPlayers] = useState([]);

    useEffect(() => {
        const fetchPlayers = async () => {
            try {
                const players = await fetchData(`game/${gameId}/players`);
                setPlayers(players);
            } catch (error) {
                console.error('Failed to fetch players:', error);
            }
        };

        fetchPlayers();
    }, []);

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
