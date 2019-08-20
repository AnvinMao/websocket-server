package com.codetc.chat.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by anvin 2019-08-20
 */
@Component
public class NettyConfig {

    @Value("${netty.server.port}")
    private int port;

    @Value("${netty.server.boss-thread}")
    private int bossThread;

    @Value("${netty.server.worker-thread}")
    private int workerThread;

    @Value("${netty.server.keepalive}")
    private boolean keepAlive;

    @Value("${netty.server.backlog}")
    private int backlog;

    @Value("${netty.server.receive-buff-size}")
    private int receiveBuffSize;

    @Value("${netty.server.send-buff-size}")
    private int sendBuffSize;

    public int getPort() {
        return this.port;
    }

    public int getBossThread() {
        return this.bossThread;
    }

    public int getWorkerThread() {
        return this.workerThread;
    }

    public boolean isKeepAlive() {
        return this.keepAlive;
    }

    public int getBacklog() {
        return this.backlog;
    }

    public int getReceiveBuffSize() {
        return this.receiveBuffSize;
    }

    public int getSendBuffSize() {
        return this.sendBuffSize;
    }
}