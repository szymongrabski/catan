package com.example.backend.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {
    private final static Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        URI uri = session.getUri();
        assert uri != null;
        String query = uri.getQuery();
        String gameId = extractGameIdFromQuery(query);
        if (gameId != null) {
            session.getAttributes().put("game-id", gameId);
            sessions.put(gameId, session);
        }
        super.afterConnectionEstablished(session);
    }

    public void notifyUserAboutFetchingPlayers(Long gameId) {
        WebSocketSession session = sessions.get(gameId.toString());
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage("fetch-players"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyAboutRedirection(Long gameId) {
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage("redirect"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private String extractGameIdFromQuery(String query) {
        if (query != null) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if ("game-id".equals(pair[0])) {
                    return pair[1];
                }
            }
        }
        return null;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = (String) session.getAttributes().get("game-id");
        if (userId != null) {
            sessions.remove(userId);
        }
        super.afterConnectionClosed(session, status);
    }
}
