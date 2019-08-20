package com.codetc.chat.server.clients;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by anvin 2019-08-20
 */
public class Client {
    private long id;
    private String nickname;
    private ChannelHandlerContext ctx;

    public Client(long id, String nickname, ChannelHandlerContext ctx) {
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

    @Override
    public String toString() {
        return "id:" + this.id + ", nickName:" + this.nickname + ", channel: " + this.ctx.channel().id();
    }
}
