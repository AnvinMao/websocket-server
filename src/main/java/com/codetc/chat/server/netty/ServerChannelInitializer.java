package com.codetc.chat.server.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by anvin 2019-08-20
 */
@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private TextWebSocketFrameHandler webSocketFrameHandler;

    @Autowired
    private RequestAuthHandler requestAuthHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(requestAuthHandler);
        pipeline.addLast(new IdleStateHandler(60, 0, 0));
        pipeline.addLast(new WebSocketServerProtocolHandler("/chat", true));
        pipeline.addLast(webSocketFrameHandler);
    }
}
