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
public class NotificationsWebSocketHandler extends TextWebSocketHandler {
    private final static Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        URI uri = session.getUri();
        assert uri != null;
        String query = uri.getQuery();
        String userId = extractUserIdFromQuery(query);
        if (userId != null) {
            session.getAttributes().put("user-id", userId);
            sessions.put(userId, session);
        }
        super.afterConnectionEstablished(session);
    }

    public void notifyUserAboutFriendRequest(Long userId) {
        WebSocketSession session = sessions.get(userId.toString());
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage("friends-request"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyUserAboutGameInvitation(Long userId) {
        WebSocketSession session = sessions.get(userId.toString());
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage("game-invitations"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyUserAboutFetchingFriends(Long userId) {
        WebSocketSession session = sessions.get(userId.toString());
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage("friends-fetch"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String extractUserIdFromQuery(String query) {
        if (query != null) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if ("user-id".equals(pair[0])) {
                    return pair[1];
                }
            }
        }
        return null;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = (String) session.getAttributes().get("user-id");
        if (userId != null) {
            sessions.remove(userId);
        }
        super.afterConnectionClosed(session, status);
    }

    public Map<String, WebSocketSession> getSessions() {
        return sessions;
    }
}
