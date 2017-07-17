package com.jalen.netty.websocket2;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.ImmediateEventExecutor;

public class WebSocketServer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    private static WebSocketServer webSocketServer = null;
    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel channel = null;

    private WebSocketServer() {
    }

    public static WebSocketServer getInstance() {
        if (webSocketServer == null) {
            synchronized (WebSocketServer.class) {
                if (webSocketServer == null) {
                    webSocketServer = new WebSocketServer();
                }
            }
        }
        return webSocketServer;
    }

    public void start(InetSocketAddress address) {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new WebSocketServerChannelInitializer(channelGroup));
            ChannelFuture channelFuture = bootstrap.bind(address).sync();
            channel = channelFuture.channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            logger.error("caught an exception when start webSocket: " + e);
        }
    }

    public void stop() {
        if (channel != null) {
            channel.close();
        }
        channelGroup.close();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public void sendInfoToAll(String info) {
        channelGroup.writeAndFlush(new TextWebSocketFrame(info));
    }

    public ChannelGroup getChannelGroup() {
        return channelGroup;
    }
}
