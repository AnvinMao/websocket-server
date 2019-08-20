package com.codetc.chat.server.message.entity;

/**
 * Created by anvin 2019-08-20
 */
public class Message {
    private int type;
    private int page;
    private int receiverId;
    private MessageEntity info;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setInfo(MessageEntity info) {
        this.info = info;
    }

    public MessageEntity getInfo() {
        return info;
    }
}
