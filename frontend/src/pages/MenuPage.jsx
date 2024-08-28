import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import PlayersList from "../components/PlayersList.jsx";
import InviteModal from "../components/InviteModal.jsx";
import {fetchData} from "../api/authenticatedApi.js";

function MenuPage() {
    const { gameId } = useParams();
    const navigate = useNavigate();
    const authToken = sessionStorage.getItem('authToken');

    const [isModalOpen, setIsModalOpen] = useState(false);
    const openModal = () => setIsModalOpen(true);
    const closeModal = () => setIsModalOpen(false);

    const [player, setPlayer] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchPlayer = async () => {
        try {
            const response = await fetchData(`game/${gameId}/player`);
            setPlayer(response);
        } catch (err) {
            setError('Failed to fetch player data');
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        if (!authToken) {
            navigate('/login');
        } else {
            fetchPlayer();
        }
    }, [authToken, gameId]);

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>{error}</div>;
    }

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
                        <button>Start game</button>
                    )}
                </div>
            </div>
        </div>
    );
}

export default MenuPage;
