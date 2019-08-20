package com.codetc.chat.server.message;

import com.codetc.chat.server.message.entity.MessageEntity;

import java.util.List;

public interface MessageService {

    void saveMessage(MessageEntity message);

    List<MessageEntity> getMessage(long senderId, long receiverId, int limit, int skip);
}
