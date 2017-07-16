package com.jalen.netty;


import java.net.InetSocketAddress;

import com.jalen.nettytest.EchoClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
            .handler(new ChannelInitializer<SocketChannel>() {
                
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                	ch.pipeline().addLast(new StringDecoder());
                	ch.pipeline().addLast(new StringEncoder());
                	ch.pipeline().addLast(new EchoClientHandler());
                }
            });
            ChannelFuture future = bootstrap.connect().sync();
            future.channel().closeFuture().sync();
            
        } finally {
            group.shutdownGracefully().sync();
        }
    }
//    public void start() throws Exception {
//		EventLoopGroup group = new NioEventLoopGroup();
//		try {
//			Bootstrap b = new Bootstrap();
//			b.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
//					.handler(new ChannelInitializer<SocketChannel>() {
//						@Override
//						protected void initChannel(SocketChannel ch) throws Exception {
//							ch.pipeline().addLast(new EchoClientHandler());
//						}
//					});
//			ChannelFuture f = b.connect().sync();
//			
//			
//			f.channel().closeFuture().sync();
//		} finally {
//			group.shutdownGracefully().sync();
//		}
//	}
    public static void main(String[] args) throws Exception {
        new EchoClient("localhost", 20000).start();
    }

}
