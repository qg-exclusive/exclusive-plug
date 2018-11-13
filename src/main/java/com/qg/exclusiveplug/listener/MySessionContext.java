package com.qg.exclusiveplug.listener;


import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * @author HuaChen
 * time:2018年11月13日15:34:39
 * description:存储Session信息
 */
public class MySessionContext {
    private static MySessionContext instance;
    private HashMap mymap;

    private MySessionContext() {
        mymap = new HashMap();
    }

    public static MySessionContext getInstance() {
        if (instance == null) {
            instance = new MySessionContext();
        }
        return instance;
    }

    public synchronized void AddSession(HttpSession session) {
        if (session != null) {
            mymap.put(session.getId(), session);
        }
    }

    public synchronized void DelSession(HttpSession session) {
        if (session != null) {
            mymap.remove(session.getId());
        }
    }

    public synchronized HttpSession getSession(String session_id) {
        System.out.println("asdsdsssss");
        if (session_id == null) return null;
        return (HttpSession) mymap.get(session_id);
    }

}