package com.hww.netty.protobuf.client;

import com.hww.netty.protobuf.handler.SubRegClientHandler;
import com.hww.serializationtechnology.protobuf.code.SubscribeRespProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.apache.log4j.Logger;

public class SubRegClient {

    private Logger logger = Logger.getLogger(SubRegClient.class);

    public static void main(String[] args) {
        int port = 8888;
        new SubRegClient().connect(port, "127.0.0.1");
    }

    public void connect(int port, String host) {
        EventLoopGroup workLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workLoopGroup).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChildChannelInitializer());
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            logger.info("the client  started in port :" + port + " and host in " + host);
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workLoopGroup.shutdownGracefully();
        }
    }

    private class ChildChannelInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) {
            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
            //解码器
            ch.pipeline().addLast(new ProtobufDecoder(SubscribeRespProto.SubscribeResp.getDefaultInstance()));
            ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
            ch.pipeline().addLast(new ProtobufEncoder());
            ch.pipeline().addLast(new SubRegClientHandler());
        }
    }
}
