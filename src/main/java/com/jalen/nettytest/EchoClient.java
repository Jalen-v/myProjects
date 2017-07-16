package com.jalen.nettytest;

import java.net.InetSocketAddress;
import java.util.Scanner;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

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
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new EchoClientHandler());
						}
					});
			ChannelFuture f = b.connect().sync();
			
          Channel channel = f.channel();
          Scanner scanner = new Scanner(System.in);
          while(true){
          	System.out.println("please input:");
          	System.out.println(f.channel().isActive());
          	channel.writeAndFlush(scanner.nextLine());
          }
			
//			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}

	public static void main(String[] args) throws Exception {
		new EchoClient("localhost", 20000).start();
	}
}
