package com.codetc.chat.server.clients;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by anvin 2019-08-20
 */
@Service
public class ClientServiceImpl implements ClientService {

    private final Map<Long, ClientSession> clients = new ConcurrentHashMap<>();

    @Override
    public void addClient(ClientSession client) {
        this.clients.put(client.getId(), client);
    }

    @Override
    public void removeClient(ChannelHandlerContext ctx) {
        this.clients.entrySet().removeIf(entry ->
                entry.getValue().getCtx().channel().id().equals(ctx.channel().id()));
        ctx.close();
        System.out.println("remove client:" + this.clients.size());
    }

    @Override
    public ClientSession getClient(long id) {
        return this.clients.get(id);
    }

    @Override
    public ClientSession getClient(ChannelHandlerContext ctx) {
        for (Map.Entry<Long, ClientSession> entry : this.clients.entrySet()) {
            if (entry.getValue().getCtx().channel().id().equals(ctx.channel().id())) {
                return entry.getValue();
            }
        }

        ctx.close();
        return null;
    }
}
