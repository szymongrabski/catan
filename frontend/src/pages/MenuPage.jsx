import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import PlayersList from "../components/PlayersList.jsx";
import InviteModal from "../components/InviteModal.jsx";

function MenuPage() {
    const { gameId } = useParams();
    const navigate = useNavigate();
    const authToken = sessionStorage.getItem('authToken');

    const [isModalOpen, setIsModalOpen] = useState(false);
    const openModal = () => setIsModalOpen(true);
    const closeModal = () => setIsModalOpen(false);

    useEffect( () => {

        if (!authToken) {
            navigate('/login');
        }
    }, [authToken]);

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
                </div>
            </div>
        </div>
    );
}

export default MenuPage;
