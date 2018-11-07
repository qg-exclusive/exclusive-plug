package com.qg.exclusiveplug.map;

import com.qg.exclusiveplug.model.User;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHolder {
    private static final Map<User, WebSocketSession> map = new ConcurrentHashMap<>(16);

    public static void put(User user, WebSocketSession webSocketSession) {
        map.put(user, webSocketSession);
    }

    public static WebSocketSession getWebSocketSession(User user) {
        return map.get(user);
    }

    public static Map<User, WebSocketSession> getMap() {
        return map;
    }

    public static void removeWebSocketSession(WebSocketSession webSocketSession) {
        map.entrySet().stream().filter(entry -> entry.getValue() == webSocketSession)
                .forEach(entry -> map.remove(entry.getKey()));
    }
}
