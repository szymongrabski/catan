import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth/authenticate';

export const login = async (username, password) => {
    try {
        const response = await axios.post(API_URL, { username, password });

        const token = response.data.token;
        console.log(token)

        sessionStorage.setItem('authToken', token);

        return true
    } catch (error) {
        console.error('Errow while logging in:', error.response?.data || error.message);
        return false;
    }
};
