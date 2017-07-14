package com.jalen.netty;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
    private final int port;
    
    public EchoServer(int port) {
        this.port = port;
    }
    

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group).channel(NioServerSocketChannel.class).localAddress(port)
                    .childHandler(new ChannelInitializer<Channel>() {

                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            final ChannelFuture future = bootstrap.bind().sync();
            System.out
                    .println(EchoServer.class.getName() + " started and listen on " + future.channel().localAddress());
            
//            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//            executorService.scheduleAtFixedRate(new Runnable() {                
//                public void run() {
//                    System.out.println("isSuccess:"+future.isSuccess()+"...isDone:"+future.isDone()+"...isCancelled:"+future.isCancelled()+"...isCancellable:"+future.isCancellable());
//                }
//            }, 5, 5, TimeUnit.SECONDS);
            
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new EchoServer(20000).start();
    }
}
