package com.codetc.chat.server.clients;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * Created by anvin 2019-08-20
 */
public class ClientSession {
    private long id;
    private String nickname;
    private ChannelHandlerContext ctx;

    public ClientSession(long id, String nickname, ChannelHandlerContext ctx) {
        this.id = id;
        this.nickname = nickname;
        this.ctx = ctx;
    }

    public long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void sendMessage(String message) {
        if (this.ctx == null) {
            return;
        }

        this.ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
    }

    @Override
    public String toString() {
        return "id:" + this.id + ", nickName:" + this.nickname + ", channel: " + this.ctx.channel().id();
    }
}
