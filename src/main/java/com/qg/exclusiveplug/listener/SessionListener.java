package com.qg.exclusiveplug.listener;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author HuaChen
 * time:2018年11月13日15:33:58
 * description:监听并存储SESSION信息
 */
@WebListener
@Slf4j
public class SessionListener implements HttpSessionListener {
    private SessionContext sessionContext = SessionContext.getInstance();

    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        log.info("创建Session");
        sessionContext.AddSession(httpSessionEvent.getSession());
    }

    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        sessionContext.DelSession(session);
    }
}
