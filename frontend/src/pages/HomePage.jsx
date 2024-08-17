import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import UserSearch from "../components/UserSearch.jsx";
import FriendList from "../components/FriendList.jsx";
import FriendRequests from "../components/FriendRequests.jsx";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEdit} from "@fortawesome/free-solid-svg-icons";
import logo from "../assets/logo.svg";
import GameInvitations from "../components/GameInvitations.jsx";
import {createGame} from "../api/authenticatedApi.js";

function HomePage() {
    const navigate = useNavigate();
    const authToken = sessionStorage.getItem('authToken');

    const handleStartGame = async () => {
        try {
            const newGame = await createGame();
            navigate(`/menu/${newGame}`)
            console.log('Game created:', newGame);
        } catch (error) {
            console.error('Failed to create game:', error);
        }
    };


    useEffect(() => {

        if (!authToken) {
            navigate('/login');
        }
    }, [authToken]);

    return (
        <div className="home-page-container">
            <div className="home-page">
                <div>
                    <FriendRequests/>
                    <GameInvitations/>
                    <FriendList unfriend={true}/>
                </div>
                <div className="content">
                    <div className="logo-container">
                        <img src={logo} alt="logo" className="logo"/>
                    </div>
                    <div className="test">
                        <div className="circle"></div>
                        <div className="edit-button">
                            <FontAwesomeIcon icon={faEdit} size="lg"/>
                        </div>
                    </div>
                    <button onClick={() => handleStartGame() }>Start game
                    </button>
                    <button onClick={() => {
                        sessionStorage.removeItem('authToken');
                        navigate('/login');
                    }}>Log out
                    </button>
                </div>
                <UserSearch/>
            </div>
        </div>
    );
}

export default HomePage;
