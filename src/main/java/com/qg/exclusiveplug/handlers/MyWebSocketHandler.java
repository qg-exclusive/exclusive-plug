package com.qg.exclusiveplug.handlers;

import com.google.gson.Gson;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.map.WebSocketHolder;
import com.qg.exclusiveplug.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author WilderGao
 * time 2018-09-30 23:27
 * motto : everything is no in vain
 * description
 */
@Service
@Slf4j
public class MyWebSocketHandler implements WebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {

        log.info("成功建立连接");
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession,
                              WebSocketMessage<?> webSocketMessage) throws Exception {
        log.info("接收信息 >> {}", webSocketMessage.getPayload());

        Map<Integer, Integer> integerIntegermap = ((User) webSocketSession.getAttributes()
                .get(String.valueOf(webSocketSession.getRemoteAddress()))).getIndexPrivilegeMap();

        int deviceIndex = Integer.valueOf(String.valueOf(webSocketMessage.getPayload()).split(":")[1]);

        log.info("切换串口：" + deviceIndex);

        // 判断权限
        if(integerIntegermap.containsKey(deviceIndex)) {
            if(WebSocketHolder.containsKey(deviceIndex)) {
                List<WebSocketSession> websocketSessionList = WebSocketHolder.getWebsocketSessionList(deviceIndex);
                if (!websocketSessionList.contains(webSocketSession)) {
                    websocketSessionList.add(webSocketSession);
                }

            } else {
                List<WebSocketSession> websocketSessionList = new ArrayList<>();
                websocketSessionList.add(webSocketSession);
                WebSocketHolder.put(deviceIndex, websocketSessionList);
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession,
                                     Throwable throwable) throws Exception {
        log.info("{}连接出现异常", webSocketSession.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession,
                                      CloseStatus closeStatus) throws Exception {
        Map<Integer, Integer> integerIntegermap = ((User) webSocketSession.getAttributes()
                .get(String.valueOf(webSocketSession.getRemoteAddress()))).getIndexPrivilegeMap();

        Iterator<Integer> integerIterator = integerIntegermap.keySet().iterator();
        while(integerIterator.hasNext()) {
            List<WebSocketSession> webSocketSessionList = WebSocketHolder.getWebsocketSessionList(integerIterator.next());
            for (WebSocketSession webSocketSession1 : webSocketSessionList) {
                if (webSocketSession1 == webSocketSession) {
                    webSocketSessionList.remove(webSocketSession1);
                }
            }
        }

        log.info("Socket会话结束，即将移除socket");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 发送某一端口的数据给在线用户
     * @param deviceIndex 产生的端口数据
     * @param responseData 返回数据
     */
    public static void send(int deviceIndex, ResponseData responseData) {
        List<WebSocketSession> websocketSessionList = WebSocketHolder.getWebsocketSessionList(deviceIndex);
        for (WebSocketSession webSocketSession : websocketSessionList) {
            try {
                webSocketSession.sendMessage(new TextMessage(new Gson().toJson(responseData)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
