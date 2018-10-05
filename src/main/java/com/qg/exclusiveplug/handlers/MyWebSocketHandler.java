package com.qg.exclusiveplug.handlers;

import com.google.gson.Gson;
import com.qg.exclusiveplug.dtos.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;
import java.io.IOException;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author WilderGao
 * time 2018-09-30 23:27
 * motto : everything is no in vain
 * description
 */
@Service
@Slf4j
public class MyWebSocketHandler implements WebSocketHandler {

    private static WebSocketSession session = null;
    private static int index = 0;

    public static int getIndex(){
        return index;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        session = webSocketSession;
        log.info("成功建立连接");
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession,
                              WebSocketMessage<?> webSocketMessage) throws Exception {
        log.info("接收信息 >> {}",webSocketMessage.getPayload());
        index = Integer.valueOf(String.valueOf(webSocketMessage.getPayload()).split(":")[1]);
        log.info("切换串口：" + index);
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession,
                                     Throwable throwable) throws Exception {
        log.info("{}连接出现异常",webSocketSession.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession,
                                      CloseStatus closeStatus) throws Exception {
        index = 0;
        session = null;
        log.info("Socket会话结束，即将移除socket");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public static void send(ResponseData responseData) {
        try {
            if(null != session){
                session.sendMessage(new TextMessage(new Gson().toJson(responseData)));
            }
        } catch (IOException e) {
            log.error("发送失败");
            e.printStackTrace();
        }
    }
}
