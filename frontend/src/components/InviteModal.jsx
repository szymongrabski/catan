import React, { useState } from 'react';
import Modal from 'react-modal';
import { sendInvitation } from '../api/authenticatedApi.js';
import {useUser} from "../context/UserContext.jsx";
import FriendList from "./FriendList.jsx";

Modal.setAppElement('#root');

const InviteModal = ({ isOpen, onRequestClose, gameId }) => {
    const { friends } = useUser();
    const [selectedFriends, setSelectedFriends] = useState([]);

    const handleFriendSelection = (friendId) => {
        setSelectedFriends(prevState =>
            prevState.includes(friendId) ?
                prevState.filter(id => id !== friendId) :
                [...prevState, friendId]
        );
    };

    const handleSendInvitations = async () => {
        try {
            await sendInvitation(gameId, selectedFriends);
            alert('Invitations sent!');
            onRequestClose();
        } catch (error) {
            console.error('Error sending invitations:', error);
            alert('Failed to send invitations');
        }
    };

    return (
        <Modal
            isOpen={isOpen}
            onRequestClose={onRequestClose}
            contentLabel="Invite Friends"
            className="modal"
            overlayClassName="overlay"
        >
            <h2>Invite Friends</h2>
            <FriendList gameId={gameId} invite={true} />
            <button onClick={onRequestClose}>Close</button>
        </Modal>
    );
};

export default InviteModal;
