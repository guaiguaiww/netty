package com.hww.netty.decoderAndEncoder.delimiterBaseFrameDecoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.log4j.Logger;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/10/30
 * @Time: 11:05
 * Description:
 */
public class EchoServer {

    private Logger logger = Logger.getLogger(EchoServer.class);

    public static void main(String[] args) {
        int port = 8888;
        new EchoServer().bind(port);
    }

    public void bind(int port){
        //用于服务端接受客户端的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //用于进行SocketChannel的网路读写
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建启动服务器的对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //backLog==积压
                    .option(ChannelOption.SO_BACKLOG, 100)
                    //.handler==>bossGroup
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //.childHandle==>workerGroup
                    .childHandler(new ChildChannelServerHandler());
            //绑定端口,同步等待成功
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            logger.info("the server has started in port :" + port);

            //等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //优雅退出，释放线程资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelServerHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            //自定义分隔符
            ByteBuf delimiter=Unpooled.copiedBuffer("$_".getBytes());
            //获取管道
            ChannelPipeline pipeline = socketChannel.pipeline();
            pipeline.addLast(new DelimiterBasedFrameDecoder(1024,delimiter));
            pipeline.addLast(new StringDecoder());
            pipeline.addLast(new EchoServerHandler());

        }
    }
}
