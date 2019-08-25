package com.codetc.chat.server.message;

import com.codetc.chat.server.message.entity.Message;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by anvin on 8/25/2019.
 */
public interface MessageHandler {

    void handler(ChannelHandlerContext ctx, String message);

    void messageForward(ChannelHandlerContext ctx, Message message);

    void historyMessage(ChannelHandlerContext ctx, Message message);
}
