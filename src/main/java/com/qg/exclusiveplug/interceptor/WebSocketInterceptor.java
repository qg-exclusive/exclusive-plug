package com.qg.exclusiveplug.interceptor;

import com.qg.exclusiveplug.listener.MySessionContext;
import com.qg.exclusiveplug.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author WilderGao
 * time 2018-09-30 23:23
 * motto : everything is no in vain
 * description
 */
@Service
@Slf4j
public class WebSocketInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                                   WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        log.info("webSocket握手请求...");
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) serverHttpRequest;
            String jSessionId = servletRequest.getServletRequest().getParameter("jSessionId");
            if (null != jSessionId && !jSessionId.equals("")) {
                MySessionContext myc = MySessionContext.getInstance();
                if (null != myc) {
                    HttpSession httpSession = myc.getSession(jSessionId);
                    if (null != httpSession) {
                        User user = (User) httpSession.getAttribute("user");
                        if (null != user) {
                            log.info("user用户：" + user);

                            map.put(serverHttpRequest.getRemoteAddress().toString(), user);
                            return true;
                        }
                    }
                }
            }

        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                               WebSocketHandler webSocketHandler, Exception e) {
        log.info("webSocket握手结束...");
    }
}
