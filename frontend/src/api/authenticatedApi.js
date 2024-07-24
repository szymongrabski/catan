import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

async function fetchData(endpoint) {
    const authToken = sessionStorage.getItem('authToken');

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

export default fetchData;
