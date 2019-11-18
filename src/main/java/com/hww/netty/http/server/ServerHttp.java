package com.hww.netty.http.server;

import com.hww.netty.http.handler.HttpServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class ServerHttp {


    public static void main(String[] args) throws InterruptedException {
        /**
         * bossGroup, 父类的事件循环组只是负责连接，获取到连接后交给 workergroup子的事件循环组，
         * 参数的获取，业务的处理等工作均是由workergroup这个子事件循环组来完成，一个事件循环组一样
         * 可以完成所有的工作，但是Netty推荐的方式是使用两个事件循环组。
         */
        //创建父事件循环组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //创建子类的事件循环组
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建启动服务器的对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            /**
             * group方法接收两个参数， 第一个为父时间循环组，第二个参数为子事件循环组
             */
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //.handler==>bossGroup
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //.childHandle==>workerGroup
                    .childHandler(new HttpServerInitializer());
            //绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    static class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            /**
             * Handler就相当于Servlet中的过滤器, 请求和响应都会走Handler
             * HttpServerCodec: http的编解码器，用于Http请求和相应
             */
            pipeline.addLast("httpServerCodec", new HttpServerCodec());
            pipeline.addLast("testHttpServerHandler", new HttpServerHandler());
        }
    }
}