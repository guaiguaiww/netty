package com.hww.netty.http.handler;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        System.out.println("ctx.channel().remoteAddress() = " + ctx.channel().remoteAddress());
        if (msg instanceof HttpRequest) {

            HttpRequest httpRequest = (HttpRequest) msg;

            System.out.println("请求方法名 = " + httpRequest.getMethod().name());

            URI uri = new URI(httpRequest.getUri());

            if ("/favicon".equals(uri.getPath())) {
                System.out.println("当前请求为请求favicon");
                return;
            }

            //要返回的内容, Channel可以理解为连接，而连接中传输的信息要为ByteBuf
            ByteBuf content = Unpooled.copiedBuffer("Hello World", CharsetUtil.UTF_8);

            //构造响应
            FullHttpResponse response =
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            //设置头信息的的MIME类型
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");  //内容类型
            //设置要返回的内容长度
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, content.readableBytes()); //内容长度
            //将响应对象返回
            ctx.writeAndFlush(response);

            ctx.channel().close();
        }
    }

}