package com.qg.exclusiveplug.map;

import io.netty.channel.ChannelId;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyChannelHolder {
    private static final Map<ChannelId, NioSocketChannel> map = new ConcurrentHashMap<>(16);

    public static void put(ChannelId channelId, NioSocketChannel socketChannel) {
        map.put(channelId, socketChannel);
    }

    public static NioSocketChannel get(ChannelId channelId) {
        return map.get(channelId);
    }

    public static Map<ChannelId, NioSocketChannel> getMAP() {
        return map;
    }

    public static void removeKey(ChannelId channelId) {
       map.remove(channelId);
    }

    public static void removeChannel(NioSocketChannel nioSocketChannel) {
        map.entrySet().stream().filter(entry -> entry.getValue() == nioSocketChannel)
                .forEach(entry -> map.remove(entry.getKey()));
    }
}
