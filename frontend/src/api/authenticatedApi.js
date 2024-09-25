import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

export async function fetchData(endpoint) {
    const authToken = sessionStorage.getItem('authToken');

    if (!authToken) {
        console.error('No authentication token found');
        throw new Error('Authentication token is missing');
    }

    const config = {
        headers: {
            Authorization: `Bearer ${authToken}`
        }
    };

    try {
        const response = await axios.get(`${API_URL}/${endpoint}`, config);

        return response.data;
    } catch (error) {
        console.error('Error fetching data:', error);
        throw error;
    }
}

export async function fetchUsers(username) {
    const endpoint = `user/search?username=${encodeURIComponent(username)}`;
    const response = await fetchData(endpoint);
    return response;
}

export async function sendInvitation(receiverId) {
    const authToken = sessionStorage.getItem('authToken');

    if (!authToken) {
        console.error('No authentication token found');
        throw new Error('Authentication token is missing');
    }

    const config = {
        headers: {
            Authorization: `Bearer ${authToken}`
        }
    };

    try {
        const response = await axios.post(`${API_URL}/friends/send`, null,  {
            ...config,
            params: {
                receiverId
            }
        });
    } catch (error) {
        console.error('Error sending invitation:', error);
        throw error;
    }
}

export async function respondToInvitation(requesterId, accept) {
    const authToken = sessionStorage.getItem('authToken');

    if (!authToken) {
        console.error('No authentication token found');
        throw new Error('Authentication token is missing');
    }

    const config = {
        headers: {
            Authorization: `Bearer ${authToken}`
        }
    };

    try {
        const response = await axios.post(`${API_URL}/friends/respond`, null,  {
            ...config,
            params: {
                requesterId,
                accept
            }
        });
    } catch (error) {
        console.error('Error responding to an invitation:', error);
        throw error;
    }
}


export async function deleteFriendship(friendId) {
    const authToken = sessionStorage.getItem('authToken');
    if (!authToken) {
        console.error('No authentication token found');
    }

    const config = {
        headers: {
            Authorization: `Bearer ${authToken}`
        }
    }

    try {
        await axios.delete(`${API_URL}/friends/delete`, {
            ...config,
            params: {
                friendId,
            }
        });
    } catch (error) {
        console.error('Error responding to an invitation:', error);
        throw error;
    }
}

export async function createGame() {
    const authToken = sessionStorage.getItem('authToken');
    if (!authToken) {
        console.error('No authentication token found');
    }

    const config = {
        headers: {
            Authorization: `Bearer ${authToken}`
        }
    }

    try {
        const response = await axios.post(`${API_URL}/game/create`, null,  config)
        return response.data;
    } catch (error) {
        console.error('Error creating game:', error);
        throw error;
    }
}

export async function startGameAPI(gameId) {
    const authToken = sessionStorage.getItem('authToken');
    if (!authToken) {
        console.error('No authentication token found');
    }

    const config = {
        headers: {
            Authorization: `Bearer ${authToken}`
        }
    }

    try {
        const response = await axios.post(`${API_URL}/game/${gameId}/start`, null,  config)
        return response.data;
    } catch (error) {
        console.error('Error creating game:', error);
        throw error;
    }
}

export async function sendGameInvitation(gameId, friendId) {
    const authToken = sessionStorage.getItem('authToken');

    if (!authToken) {
        console.error('No authentication token found');
    }

    const config = {
        headers: {
            Authorization: `Bearer ${authToken}`
        }
    }

    try {
        const response = await axios.post(`${API_URL}/invitation/send`, null,  {
            ...config,
            params: {
                gameId,
                friendId
            }
        })
        return response.data;
    } catch (error) {
        console.error('Error sending invitation to game:', error);
        throw error;
    }
}

export async function respondToGameInvitation(invitationId, accept) {
    const authToken = sessionStorage.getItem('authToken');
    if (!authToken) {
        console.error('No authentication token found');
    }

    const config = {
        headers: {
            Authorization: `Bearer ${authToken}`
        }
    }

    try {
        const response = await axios.post(`${API_URL}/invitation/respond`, null,  {
            ...config,
            params: {
                invitationId,
                accept
            }
        });
        return response;
    } catch (error) {
        console.error('Error responding to an invitation:', error);
        throw error;
    }
}

export async function placeSettlement(gameId, playerId, q, r, direction) {
    const authToken = sessionStorage.getItem('authToken');
    if (!authToken) {
        console.error('No authentication token found');
    }

    const config = {
        headers: {
            Authorization: `Bearer ${authToken}`
        }
    }

    try {
        const response = await axios.post(`${API_URL}/game/${gameId}/settlements`, null,  {
            ...config,
            params: {
                playerId,
                q,
                r,
                direction
            }
        });
        return response;
    } catch (error) {
        console.error('Error while creating settlement:', error);
        throw error;
    }
}

export async function placeRoad(gameId, playerId, road) {
    const authToken = sessionStorage.getItem('authToken');
    if (!authToken) {
        console.error('No authentication token found');
    }

    const config = {
        headers: {
            Authorization: `Bearer ${authToken}`
        }
    }

    try {
        const response = await axios.post(`${API_URL}/game/${gameId}/${playerId}/roads`, road,  config);
        return response;
    } catch (error) {
        console.error('Error while creating road:', error);
        throw error;
    }
}

export async function rollDiceForResources(gameId) {
    const authToken = sessionStorage.getItem('authToken');
    if (!authToken) {
        console.error('No authentication token found');
    }

    const config = {
        headers: {
            Authorization: `Bearer ${authToken}`
        }
    }

    try {
        const response = await axios.post(`${API_URL}/game/${gameId}/dice`, null,  config);
        console.log("rolluje" + response)
        console.log(response)
        return response;
    } catch (error) {
        console.error('Error while creating road:', error);
        throw error;
    }
}

export async function setNextPLayer(gameId) {
    const authToken = sessionStorage.getItem('authToken');
    if (!authToken) {
        console.error('No authentication token found');
    }

    const config = {
        headers: {
            Authorization: `Bearer ${authToken}`
        }
    }

    try {
        const response = await axios.post(`${API_URL}/game/${gameId}/next`, null,  config);
        return response;
    } catch (error) {
        console.error('Error while creating road:', error);
        throw error;
    }
}

export async function upgradeSettlement(gameId, playerId, q, r, direction) {
    const authToken = sessionStorage.getItem('authToken');
    if (!authToken) {
        console.error('No authentication token found');
    }

    const config = {
        headers: {
            Authorization: `Bearer ${authToken}`
        }
    }

    try {
        const response = await axios.post(`${API_URL}/game/${gameId}/${playerId}/settlements/upgrade`, {
            q, r, direction
        },  config);
        return response;
    } catch (error) {
        console.error('Error while updating vertex:', error);
        throw error;
    }
}

export async function substractResources(gameId, playerId, resourcesToGive) {
    const authToken = sessionStorage.getItem('authToken');
    if (!authToken) {
        console.error('No authentication token found');
    }

    const config = {
        headers: {
            Authorization: `Bearer ${authToken}`,
            'Content-Type': 'application/json'
        }
    }

    try {
        const response = await axios.patch(
            `${API_URL}/game/${gameId}/${playerId}/resources`,
            JSON.stringify(resourcesToGive),
            config);
        return response;
    } catch (error) {
        console.error('Error while substracting resources:', error);
        throw error;
    }
}

export async function placeRobber(gameId, q, r) {
    const authToken = sessionStorage.getItem('authToken');
    if (!authToken) {
        console.error('No authentication token found');
    }

    const config = {
        headers: {
            Authorization: `Bearer ${authToken}`,
        }
    }

    const data = { q, r };

    try {
        const response = await axios.patch(
            `${API_URL}/game/${gameId}/robber`,
            data,
            config);
        return response;
    } catch (error) {
        console.error('Error while placing robber:', error);
        throw error;
    }
}


