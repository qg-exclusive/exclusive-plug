package com.qg.exclusiveplug.listener;


import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuaChen
 * time:2018年11月13日15:34:39
 * description:存储Session信息
 */
public class SessionContext {
    private static SessionContext instance;
    private Map<String, HttpSession> map;

    private SessionContext() {
        map = new HashMap<>();
    }

    public static SessionContext getInstance() {
        if (instance == null) {
            instance = new SessionContext();
        }
        return instance;
    }

    synchronized void AddSession(HttpSession session) {
        if (session != null) {
            map.put(session.getId(), session);
        }
    }

    synchronized void DelSession(HttpSession session) {
        if (session != null) {
            map.remove(session.getId());
        }
    }

    public synchronized HttpSession getSession(String session_id) {
        if (session_id == null) return null;
        return map.get(session_id);
    }
}