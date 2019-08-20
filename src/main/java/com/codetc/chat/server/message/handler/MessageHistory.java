package com.codetc.chat.server.message.handler;

import com.codetc.chat.server.clients.Client;
import com.codetc.chat.server.message.MessageService;
import com.codetc.chat.server.message.entity.Message;
import com.codetc.chat.server.message.entity.MessageEntity;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by anvin 2019-08-20
 */
@Component
public class MessageHistory {

    @Autowired
    private MessageService messageService;

    @Autowired
    private Gson gson;

    public void execute(Client client, Message message) {
        int page = message.getPage() <= 0 ? 1 : message.getPage();
        int limit = 20;
        int skip = (page - 1) * limit;
        List<MessageEntity> list = this.messageService.getMessage(
                client.getId(),
                message.getReceiverId(),
                limit,
                skip);

        if (client.getCtx() != null) {
            client.getCtx().writeAndFlush(this.gson.toJson(list));
        }
    }
}
