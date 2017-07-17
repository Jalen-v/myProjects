package com.jalen.netty.websocket2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;

import java.net.URI;

/**
 * this class is used to test
 */
public class WebSocketClient {
    private int port;
    private String url;
    private NioEventLoopGroup group;
    private Channel channel = null;

    public WebSocketClient(int port) {
        this.port = port;
        this.url = System.getProperty("url", "ws://localhost:" + String.valueOf(port) + "/ws");
    }

    public void start() throws Exception {
        final URI uri = new URI(url);
        final int port = uri.getPort();
        group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();


                p.addLast("EncodeAndDecodeToFromByte", new HttpClientCodec());
                p.addLast("OnlyFullHttpResponse", new HttpObjectAggregator(65536));
                p.addLast(new WebSocketClientProtocolHandler(WebSocketClientHandshakerFactory
                        .newHandshaker(uri, WebSocketVersion.V13, null, false, new DefaultHttpHeaders())));
                p.addLast("WebSocketClientFrameHandler", new WebSocketClientFrameHandler());
            }
        });
        channel = b.connect(uri.getHost(), port).sync().channel();
    }

    public void write(String msg) {
        WebSocketFrame frame = new TextWebSocketFrame(msg);
        channel.writeAndFlush(frame);
    }

    public void ping() {
        WebSocketFrame frame = new PingWebSocketFrame();
        channel.writeAndFlush(frame);
    }

    public void close() throws Exception {
        channel.writeAndFlush(new CloseWebSocketFrame());
        channel.closeFuture().sync();
        group.shutdownGracefully();
        channel.closeFuture().sync();
    }
}
