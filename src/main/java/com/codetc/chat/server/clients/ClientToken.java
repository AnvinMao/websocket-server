package com.codetc.chat.server.clients;

/**
 * Created by anvin 2019-08-20
 */
public class ClientToken {
    private long userId;
    private String nickname;
    private int type;
    private long expired;

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setExpired(long expired) {
        this.expired = expired;
    }

    public long getExpired() {
        return expired;
    }
}
