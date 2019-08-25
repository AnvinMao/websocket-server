package com.codetc.chat.server.clients;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by anvin 2019-08-20
 */
public interface ClientService {

    void addClient(ClientSession client);

    void removeClient(ChannelHandlerContext ctx);

    ClientSession getClient(long id);

    ClientSession getClient(ChannelHandlerContext ctx);
}
