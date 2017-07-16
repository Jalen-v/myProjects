package com.jalen.netty.websocket;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.ImmediateEventExecutor;

/**
 * 访问地址：http://localhost:2048
 * 
 * @author c.k
 * 
 */
public class ChatServer {

	private final static ChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
	private final EventLoopGroup workerGroup = new NioEventLoopGroup();
	private Channel channel;

	public ChannelFuture start(InetSocketAddress address) {
		ServerBootstrap b = new ServerBootstrap();
		b.group(workerGroup).channel(NioServerSocketChannel.class).childHandler(createInitializer(group));
		ChannelFuture f = b.bind(address).syncUninterruptibly();
		channel = f.channel();
		return f;
	}

	public void destroy() {
		if (channel != null)
			channel.close();
		group.close();
		workerGroup.shutdownGracefully();
	}

	protected ChannelInitializer<Channel> createInitializer(ChannelGroup group) {
		return new ChatServerInitializer(group);
	}

	public static void main(String[] args) {
		final ChatServer server = new ChatServer();
		ChannelFuture f = server.start(new InetSocketAddress(2048));
		
		
      ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
      executorService.scheduleAtFixedRate(new Runnable() {                
          public void run() {
        	  getChannelsStatus();
          }
      }, 5, 5, TimeUnit.SECONDS);
      
		f.channel().closeFuture().syncUninterruptibly();
	}
	
	public static void getChannelsStatus(){
		System.out.println("group.size:"+group.size());
		for (Channel channel : group) {
			System.out.println(channel.hashCode()+":"+channel.isActive());
			
		}
		group.writeAndFlush(new TextWebSocketFrame("hello "+new Date()));
	}

}
