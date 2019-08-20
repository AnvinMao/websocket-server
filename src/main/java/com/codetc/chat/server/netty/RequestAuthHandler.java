package com.codetc.chat.server.netty;

import com.codetc.chat.server.clients.Client;
import com.codetc.chat.server.clients.ClientService;
import com.codetc.chat.server.clients.ClientToken;
import com.codetc.chat.server.utils.CryptoHelper;
import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Token验证
 * Created by anvin 2019-08-20
 */
@Component
@ChannelHandler.Sharable
public class RequestAuthHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String TOKEN_KEY = "token";

    @Value("${server.auth.password}")
    private String authPassword;

    @Autowired
    private Gson gson;

    @Autowired
    private ClientService clientService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        this.handlerHttpRequest(ctx, request);
    }

    private void handlerHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (!request.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, request, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
        Map<String, List<String>> parameters = queryStringDecoder.parameters();
        if (parameters.size() == 0 || !parameters.containsKey(this.TOKEN_KEY)) {
            sendHttpResponse(ctx, request, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        String token = parameters.get(this.TOKEN_KEY).get(0);
        if (token.isEmpty() || !this.authToken(token, ctx)) {
            sendHttpResponse(ctx, request, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        ctx.fireChannelRead(request.retain());
    }

    private boolean authToken(String token, ChannelHandlerContext ctx) {
        try {
            String jsonData = CryptoHelper.decryptWithAes(this.authPassword, token);
            ClientToken data = this.gson.fromJson(jsonData, ClientToken.class);
            if (data.getExpired() > System.currentTimeMillis()) {
                Client client = new Client(data.getUserId(), data.getNickname(), ctx);
                this.clientService.addClient(client);
                return true;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void sendHttpResponse (ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpUtil.setContentLength(res, res.content().readableBytes());
        }

        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
