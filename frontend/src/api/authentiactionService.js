import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth';

export const login = async (username, password, setError) => {
    try {
        const response = await axios.post(`${API_URL}/authenticate`, { username, password });

        const { token } = response.data;
        sessionStorage.setItem('authToken', token);

        return true;
    } catch (error) {
        const errorMessage = error.response?.data?.message || 'An error occurred';

        setError(errorMessage);
        return false;
    }
};

export const register = async (username, email, password, setError) => {
    try {
        const response = await axios.post(`${API_URL}/register`, { username, email, password });

        const { token } = response.data;

        sessionStorage.setItem('authToken', token);

        return true;
    } catch (error) {
        const errorMessage = error.response?.data?.message || 'An error occurred';

        setError(errorMessage);
        return false;
    }
};
