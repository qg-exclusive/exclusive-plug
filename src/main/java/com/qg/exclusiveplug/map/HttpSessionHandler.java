package com.qg.exclusiveplug.map;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpSessionHandler {
    private static final Map<Integer, HttpSession> map = new ConcurrentHashMap<>(16);

    public static void put(Integer integer, HttpSession httpSession) {
        map.put(integer, httpSession);
    }

    public static HttpSession getWebSocketSession(Integer integer) {
        return map.get(integer);
    }

    public static Map<Integer, HttpSession> getMap() {
        return map;
    }

    public static void removeWebSocketSession(HttpSession httpSession) {
        map.entrySet().stream().filter(entry -> entry.getValue() == httpSession)
                .forEach(entry -> map.remove(entry.getKey()));
    }
}
