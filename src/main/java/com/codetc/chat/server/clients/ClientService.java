package com.codetc.chat.server.clients;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by anvin 2019-08-20
 */
public interface ClientService {

    void addClient(Client client);

    void removeClient(ChannelHandlerContext ctx);

    Client getClient(long id);

    Client getClient(ChannelHandlerContext ctx);
}
