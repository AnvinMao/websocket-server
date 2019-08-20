package com.codetc.chat.server.message.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created by anvin 2019-08-20
 */
@Document(collection = "chat_message")
public class MessageEntity implements Serializable {

    @Indexed
    private long senderId;

    private String senderName;

    @Indexed
    private long receiverId;

    private String receiverName;

    private String message;

    @Indexed
    private Long time;

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
