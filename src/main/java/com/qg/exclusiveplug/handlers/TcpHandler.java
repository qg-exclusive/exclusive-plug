package com.qg.exclusiveplug.handlers;

import com.qg.exclusiveplug.enums.StateEnum;
import com.qg.exclusiveplug.exception.ExclusivePlugException;
import com.qg.exclusiveplug.service.TcpService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
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
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s)
            throws Exception {
        log.info("客户端请求，Id为： "+channelHandlerContext.channel().id());
        log.info("收到客户端发来的消息: "+s);
        sendPongMsg(channelHandlerContext);
        int result = tcpService.messageHandler(s);
        if (result == StateEnum.OK.getStatus()){
            log.info("数据成功保存到map中");
        }else {
            log.error("数据出错");
            channelHandlerContext.close();
            //TODO 干嘛用的。。
            throw new ExclusivePlugException(StateEnum.ANALYSIS_ERROR);
        }
        channelHandlerContext.channel().flush();
    }

    private void sendPongMsg(ChannelHandlerContext context) {
        context.writeAndFlush(PONG_MSG);
        System.out.println("sent pong msg to " + context.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
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
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // IdleStateHandler 所产生的 IdleStateEvent 的处理逻辑.
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    handleReaderIdle(ctx);
                    break;
                default:
                    log.info("TCP读写失败!");
                    break;
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        this.ctx = ctx;
        log.info("已经连接上了 : "+ctx.channel().remoteAddress());
        ctx.fireChannelActive();
        if (log.isDebugEnabled()) {
            log.debug(ctx.channel().remoteAddress() + " ");
        }
    }



    protected void handleReaderIdle(ChannelHandlerContext ctx) {
        System.err.println("---client " + ctx.channel().remoteAddress().toString() + " reader timeout, close it---");
        ctx.close();
    }



    public void send(String message){
        ctx.channel().writeAndFlush(message);
    }
}
