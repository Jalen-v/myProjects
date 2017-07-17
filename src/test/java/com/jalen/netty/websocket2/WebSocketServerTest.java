package com.jalen.netty.websocket2;

import static org.junit.Assert.assertEquals;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebSocketServerTest {

    @Test
    public void testGetInstance() {
        WebSocketServer server1 = WebSocketServer.getInstance();
        WebSocketServer server2 = WebSocketServer.getInstance();
        assertEquals(server1, server2);
        String str1 = "123";
        String str2 = new String("123");
        assertEquals(str1, str2);
    }

    @Test
    public void testStart() throws Exception {
        final WebSocketServer server = WebSocketServer.getInstance();
        new Thread(new Runnable() {
            public void run() {
                server.start(new InetSocketAddress("localhost", 2048));
            }
        }).start();

        getChannelsStatus(server.getChannelGroup());
        Thread.sleep(3000);
        System.out.println("begin:");
        for (int i = 0; i < 10; i++) {
            server.sendInfoToAll("hi....." + new Date());
        }

        Thread.sleep(1000 * 6);
        server.stop();
        Thread.sleep(1000 * 16);
        
    }

    public void getChannelsStatus(final ChannelGroup channelGroup) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            public void run() {
                System.out.println("group.size:" + channelGroup.size());
                for (Channel channel : channelGroup) {
                    System.out.println(channel.hashCode() + ":" + channel.isActive());

                }
                channelGroup.writeAndFlush(new TextWebSocketFrame("hello " + new Date()));
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

}
