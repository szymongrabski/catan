import {useNavigate, useParams} from "react-router-dom";
import {useEffect} from "react";
import FriendList from "../components/FriendList.jsx";
import PlayersList from "../components/PlayersList.jsx";

function MenuPage() {
    const { gameId } = useParams();
    const navigate = useNavigate();
    const authToken = sessionStorage.getItem('authToken');

    useEffect( () => {

        if (!authToken) {
            navigate('/login');
        }
    }, [authToken]);

    return (
        <div className="page">
            <div>
                <h1>Menu</h1>
            </div>
            {gameId && (
                <FriendList invite={true} gameId={gameId}/>
            )}
            <div>
                <h3>Players</h3>
                <PlayersList gameId={gameId}/>
            </div>
        </div>
    );
}

export default MenuPage;
