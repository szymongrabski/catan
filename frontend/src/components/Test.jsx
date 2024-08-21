import React, { useEffect, useState } from 'react';

const Test = () => {
    const [message, setMessage] = useState('');
    const [socket, setSocket] = useState(null);

    useEffect(() => {
        const ws = new WebSocket('ws://localhost:8080/ws/hello');

        ws.onopen = () => {
            console.log('Connected to WebSocket server');
        };

        ws.onmessage = (event) => {
            const data = event.data; // Odczytuj dane jako tekst
            console.log('Message received from server:', data);
            setMessage(data || 'No message received');
        };

        ws.onerror = (error) => {
            console.error('WebSocket error:', error);
        };

        ws.onclose = () => {
            console.log('Disconnected from WebSocket server');
        };

        // Ustaw WebSocket w stanie komponentu
        setSocket(ws);

        // Cleanup na odmontowanie komponentu
        return () => {
            if (ws) {
                ws.close();
            }
        };
    }, []);

    return (
        <div>
            <h1>WebSocket Message:</h1>
            <p>{message}</p>
        </div>
    );
};

export default Test;
