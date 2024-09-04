package com.example.backend.config;

import com.example.backend.websocket.GameWebSocketHandler;
import com.example.backend.websocket.NotificationsWebSocketHandler;
import com.example.backend.websocket.HelloWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new HelloWebSocketHandler(), "/ws/hello")
                .setAllowedOrigins("http://localhost:5173");
        registry.addHandler(new NotificationsWebSocketHandler(), "/ws/notifications")
                .setAllowedOrigins("http://localhost:5173");
        registry.addHandler(new GameWebSocketHandler(), "/ws/game")
                .setAllowedOrigins("http://localhost:5173");
    }
}

