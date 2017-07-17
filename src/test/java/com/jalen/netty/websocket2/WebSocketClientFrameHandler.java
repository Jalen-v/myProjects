package com.jalen.netty.websocket2;

import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.*;

public class WebSocketClientFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private Channel ch;

    public WebSocketClientFrameHandler() {
    }

    @Override
    public void channelRead0(ChannelHandlerContext context, WebSocketFrame frame) {
        /**
         * Ping and close has been processed automatically by the WebSocketClientProtocolHandler
         */
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            System.out.println("Client received : " + textFrame.text());
            context.channel().writeAndFlush(new PingWebSocketFrame());
            frame.retain();
        } else {
            String message = "unsupport frame type = " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext context, Object evt) throws Exception {
        if (evt == WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            ch = context.channel();
            System.out.println("client has been connected");
        } else {
            super.userEventTriggered(context, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable t) {
        context.close();
        t.printStackTrace();
    }
}
