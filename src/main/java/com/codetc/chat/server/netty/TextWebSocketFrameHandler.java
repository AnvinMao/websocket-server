package com.codetc.chat.server.netty;

import com.codetc.chat.server.clients.Client;
import com.codetc.chat.server.clients.ClientService;
import com.codetc.chat.server.message.MessageType;
import com.codetc.chat.server.message.entity.Message;
import com.codetc.chat.server.message.handler.MessageForward;
import com.codetc.chat.server.message.handler.MessageHistory;
import com.google.gson.Gson;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by anvin 2019-08-20
 */
@Component
@ChannelHandler.Sharable
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    private ClientService clientService;

    @Autowired
    private MessageForward messageForward;

    @Autowired
    private MessageHistory messageHistory;

    @Autowired
    private Gson gson;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        String msg = frame.text();
        if (msg.isEmpty()) return;

        Message message = this.gson.fromJson(msg, Message.class);
        switch (message.getType()) {
            case MessageType.HEART_BEAT:    //心跳
                break;
            case MessageType.HISTORY_MESSAGE:   //获取聊天记录
                Client client = this.clientService.getClient(ctx);
                if (client == null) {
                    this.clientService.removeClient(ctx);
                    return;
                }

                if (message.getReceiverId() != 0) {
                    this.messageHistory.execute(client, message);
                }
                break;
            case MessageType.SEND_MESSAGE:  //发送信息
                if (message.getInfo() == null || message.getInfo().getReceiverId() == 0) {
                    return;
                }

                Client targetClient = this.clientService.getClient(
                        message.getInfo().getReceiverId());
                this.messageForward.execute(targetClient, message);
                break;
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent stateEvent = (IdleStateEvent) evt;
            if (stateEvent.state().equals(IdleState.READER_IDLE)) {
                ctx.close();
            }
        }

        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.clientService.removeClient(ctx);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        this.clientService.removeClient(ctx);
        super.exceptionCaught(ctx, cause);
    }
}
