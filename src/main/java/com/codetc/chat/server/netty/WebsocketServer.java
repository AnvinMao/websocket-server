package com.codetc.chat.server.netty;

import com.codetc.chat.server.config.NettyConfig;
import com.codetc.chat.server.utils.CryptoHelper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by anvin 2019-08-20
 */
@Component
public class WebsocketServer {

    @Autowired
    private NettyConfig config;

    @Autowired
    private ServerChannelInitializer channelInitializer;

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(config.getBossThread());
        EventLoopGroup workerGroup = new NioEventLoopGroup(config.getWorkerThread());
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(channelInitializer)
                    .option(ChannelOption.SO_BACKLOG, config.getBacklog())
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_RCVBUF, config.getReceiveBuffSize())
                    .childOption(ChannelOption.SO_SNDBUF, config.getSendBuffSize())
                    .childOption(ChannelOption.SO_KEEPALIVE, config.isKeepAlive())
                    .childOption(ChannelOption.TCP_NODELAY, true);

            ChannelFuture future = bootstrap.bind(config.getPort()).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
