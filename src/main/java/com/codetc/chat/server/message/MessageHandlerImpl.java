package com.codetc.chat.server.message;

import com.codetc.chat.server.clients.ClientSession;
import com.codetc.chat.server.clients.ClientService;
import com.codetc.chat.server.message.entity.Message;
import com.codetc.chat.server.message.entity.MessageEntity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by anvin on 8/25/2019.
 */
@Service
public class MessageHandlerImpl implements MessageHandler {

    private static final Logger log = LoggerFactory.getLogger(MessageHandlerImpl.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private Gson gson;

    @Autowired
    private ClientService clientService;

    @Value("${message.page.size}")
    private int messagePageSize;

    @Override
    public void handler(ChannelHandlerContext ctx, String msg) {
        Message message;
        try {
            message = this.gson.fromJson(msg, Message.class);
        } catch (JsonSyntaxException e) {
            return;
        }

        switch (message.getType()) {
            case MessageType.HISTORY_MESSAGE:   //获取聊天记录
                this.historyMessage(ctx, message);
                break;
            case MessageType.SEND_MESSAGE:      //发送信息
                this.messageForward(ctx, message);
                break;
            default:
                log.error("Unsupported message type: {}", msg);
        }
    }

    @Override
    public void messageForward(ChannelHandlerContext ctx, Message message) {
        if (message.getReceiverId() == 0 || message.getMessage().isEmpty()) {
            return;
        }

        ClientSession sender = this.clientService.getClient(ctx);
        if (sender == null) {
            this.clientService.removeClient(ctx);
            return;
        }

        MessageEntity entity = new MessageEntity(
                sender.getId(),
                sender.getNickname(),
                message.getReceiverId(),
                message.getReceiverName(),
                message.getMessage(),
                System.currentTimeMillis());
        this.messageService.saveMessage(entity);

        ClientSession receiver = this.clientService.getClient(message.getReceiverId());
        if (receiver != null) {
            receiver.sendMessage(this.gson.toJson(entity));
        }
    }

    @Override
    public void historyMessage(ChannelHandlerContext ctx, Message message) {
        if (message.getReceiverId() == 0) {
            return;
        }

        ClientSession client = this.clientService.getClient(ctx);
        if (client == null) {
            this.clientService.removeClient(ctx);
            return;
        }

        int page = message.getPage() <= 0 ? 1 : message.getPage();
        int skip = (page - 1) * this.messagePageSize;
        List<MessageEntity> list = this.messageService.getMessage(
                client.getId(),
                message.getReceiverId(),
                this.messagePageSize,
                skip);

        client.sendMessage(this.gson.toJson(list));
    }
}
