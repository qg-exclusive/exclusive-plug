package com.qg.exclusiveplug.handlers;

import com.qg.exclusiveplug.service.TcpService;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author WilderGao
 * time 2018-09-20 16:43
 * motto : everything is no in vain
 * description
 */
@Component
@Qualifier("serverHandler")
@ChannelHandler.Sharable
@Slf4j
public class TcpHandler extends SimpleChannelInboundHandler<String> {
    private static final String PONG_MSG = "2";

    @Autowired
    private TcpService tcpService;

    private static ChannelHandlerContext ctx = null;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String message) {
        log.info("客户端请求，Id为： " + channelHandlerContext.channel().id());
        log.info("收到客户端发来的消息: " + message);

        sendPongMsg(channelHandlerContext);

        if (tcpService.messageHandler(message)) {
            log.info("数据成功保存到map中");
        } else {
            log.error("数据出错");
            channelHandlerContext.close();
        }
        channelHandlerContext.channel().flush();
    }

    private void sendPongMsg(ChannelHandlerContext context) {
        ChannelFuture channelFuture = context.writeAndFlush(PONG_MSG);
        channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            if (channelFuture1.isSuccess()) {
                log.info("发送PONG信息成功：" + context.channel().remoteAddress());
            } else {
                log.error("发送PONG信息失败：" + context.channel().remoteAddress());
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Throwable t = cause.getCause();
        if (t instanceof ReadTimeoutException) {
            log.info("read time out");
        } else if (t instanceof WriteTimeoutException) {
            log.info("write time out");
        }
        log.info("连接出现了异常 ... ");
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("断开连接 ... ");
        super.channelInactive(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("读写超时，检测心跳 >> {}", ctx.channel().remoteAddress());
        // IdleStateHandler 所产生的 IdleStateEvent 的处理逻辑.
        if (evt instanceof IdleStateEvent) {
            log.info("检查通道连接状态 >> {}", ctx.channel().remoteAddress());
            ctx.writeAndFlush(PONG_MSG).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
//            ctx.close();
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        log.info("已经连接上了 : " + ctx.channel().remoteAddress() + ":" + ctx.channel().id());
        ctx.fireChannelActive();
    }


    private void handleReaderIdle(ChannelHandlerContext ctx) {
        System.err.println("---client " + ctx.channel().remoteAddress().toString() + " reader timeout, close it---");
        ctx.close();
    }


    public void send(String message) {
        ctx.channel().writeAndFlush(message);
    }
}
