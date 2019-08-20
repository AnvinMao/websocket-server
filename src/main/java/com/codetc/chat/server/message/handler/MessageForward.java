package com.codetc.chat.server.message.handler;

import com.codetc.chat.server.clients.Client;
import com.codetc.chat.server.message.MessageService;
import com.codetc.chat.server.message.entity.Message;
import com.codetc.chat.server.message.entity.MessageEntity;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by anvin 2019-08-20
 */
@Component
public class MessageForward {

    @Autowired
    private MessageService messageService;

    @Autowired
    private Gson gson;

    public void execute(Client client, Message message) {
        MessageEntity entity = message.getInfo();
        entity.setTime(System.currentTimeMillis());
        this.messageService.saveMessage(entity);

        if (client != null) {
            client.getCtx().writeAndFlush(this.gson.toJson(entity));
        }
    }
}
