package com.jalen.netty.websocket2;

import org.junit.Test;

import com.jalen.thrift.AdditionService.Client;

public class WebSocketClientTest {

    @Test
    public void testStart() throws Exception {
        for (int i = 0; i < 1000; i++) {
            WebSocketClient client = new WebSocketClient(2048);
            client.start();
        }
        Thread.sleep(1000 * 60);

    }

    @Test
    public void testStart2() throws Exception {
        WebSocketClient client = new WebSocketClient(2048);
        client.start();
        Thread.sleep(1000 * 10);

    }
}
