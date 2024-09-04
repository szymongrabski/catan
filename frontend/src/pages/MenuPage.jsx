import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import PlayersList from "../components/PlayersList.jsx";
import InviteModal from "../components/InviteModal.jsx";

import {useGame} from "../context/GameContext.jsx";
import {startGameAPI} from "../api/authenticatedApi.js";
import board from "../components/Board.jsx";

function MenuPage() {
    const { gameId } = useParams();
    const { setGameId, player, loading, error, setError, isReady, setCurrentPlayerIndex } = useGame()
    const navigate = useNavigate();
    const authToken = sessionStorage.getItem('authToken');

    const [isModalOpen, setIsModalOpen] = useState(false);
    const openModal = () => setIsModalOpen(true);
    const closeModal = () => setIsModalOpen(false);


    useEffect(() => {
        setGameId(gameId);
    }, []);


    useEffect(() => {
        if (!authToken) {
            navigate('/login');
        }

        if (isReady) {
            navigate(`/${gameId}/game`);
        }
    }, [authToken, navigate, isReady]);

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>{error}</div>;
    }

    const start = async () => {
        try {
            const response = await startGameAPI(gameId);
            setGameId(response.currentPlayerIndex);
            navigate(`/${gameId}/game`);
        } catch (error) {
            setError(error);
        }
    };

    return (
        <div className="page2">
            <div>
                <h1>Menu</h1>
            </div>
            <InviteModal
                isOpen={isModalOpen}
                onRequestClose={closeModal}
                gameId={gameId}
            />
            <div>
                <div>
                    <h3>Players</h3>
                    <PlayersList gameId={gameId}/>
                </div>
                <div>
                    <button onClick={openModal}>Invite</button>
                    {player && player.role === "ADMIN" && (
                        <button onClick={start}>Start game</button>
                    )}
                </div>
            </div>
        </div>
    );
}

export default MenuPage;
