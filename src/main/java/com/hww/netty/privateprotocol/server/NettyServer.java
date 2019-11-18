package com.hww.netty.privateprotocol.server;

import com.hww.netty.privateprotocol.common.NettyConstant;
import com.hww.netty.privateprotocol.decoder.NettyMessageDecoder;
import com.hww.netty.privateprotocol.encoder.NettyMessageEncoder;
import com.hww.netty.privateprotocol.handler.HeartBeatRespHandler;
import com.hww.netty.privateprotocol.handler.LoginAuthRespHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/11/13
 * @Time: 17:53
 * Description:
 */
public class NettyServer {

    private Logger logger = Logger.getLogger(HeartBeatRespHandler.class);

    public void bind() throws Exception {
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws IOException {
                            ch.pipeline().addLast(new NettyMessageEncoder());
                            ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4, -8, 0));
                            //ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
                            ch.pipeline().addLast("HeartBeatHandler", new HeartBeatRespHandler());
                            ch.pipeline().addLast(new LoginAuthRespHandler());
                        }
                    });
            // 绑定端口，同步等待成功
            ChannelFuture f = b.bind(8888).sync();
            logger.info("Netty server start ok : " + (NettyConstant.REMOTEIP + " : " + NettyConstant.PORT));
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyServer().bind();
    }
}
