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