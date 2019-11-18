package com.hww.netty.websocket.server;

import com.hww.netty.websocket.handler.WebSocketServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {

    public static void main(String[] args) throws Exception{
        int port = 8080;
        new WebSocketServer().run(port);
    }

    public void run(int port) throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChildChannelInitializer());
            Channel ch = bootstrap.bind(port).sync().channel();
            System.out.println("web socket server started at port " + port + ".");
            System.out.println("open your browser and navigate to http://localhost:" + port + "/");
            ch.closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();

            /**
             * HttpServerCodec:将请求或者应答消息编码或者解码为Http消息
             * HttpObjectAggregator：将http消息的多个部分组合成一条完整的http消息
             * ChunkedWriteHandler：用于支持浏览器和服务端进行WebSocket通信
             */
            pipeline.addLast("http-codec",new HttpServerCodec());
            pipeline.addLast("aggregator",new HttpObjectAggregator(65536));
            pipeline.addLast("http-chunked",new ChunkedWriteHandler());
            pipeline.addLast("handler",new WebSocketServerHandler());
        }

    }
}
