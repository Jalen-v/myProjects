package com.jalen.netty;

import java.util.ArrayList;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class EchoServerHandler extends ChannelInboundHandlerAdapter{
    private static ChannelGroup allChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static ArrayList<String> arrayList = new ArrayList<String>();
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    	System.out.println(ctx.channel().isActive());
//        allChannels.add(ctx.channel());
//        System.out.println(allChannels.size());
//        arrayList.add("str1");
//        System.out.println(arrayList.size());
        ctx.channel().writeAndFlush("hello client");
        System.out.println(ctx.channel().isActive());
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server received: " + msg);
        ctx.writeAndFlush(msg);
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
