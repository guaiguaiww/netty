package com.hww.netty.tcpquestion.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.apache.log4j.Logger;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/10/28
 * @Time: 16:23
 * Description:
 */
public class TcpTimeClient {

    private Logger logger = Logger.getLogger(TcpTimeClient.class);

    public static void main(String[] args) {
        int port = 8889;
        new TcpTimeClient().connect(port, "127.0.0.1");
    }

    public void connect(int port, String host) {
        //客户端nio线程组
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            //客户端辅助启动类
            Bootstrap b = new Bootstrap();
            b.group(workGroup).channel(NioSocketChannel.class)
                    //设置tcp参数
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new TcpChildChannelClientHandler());
            ChannelFuture channelFuture = b.connect(host, port).sync();

            logger.info("the client  started in port :" + port + " and host in " + host);

            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
        }
    }

    //处理网络io事件
    private class TcpChildChannelClientHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel socketChannel)  {
            ChannelPipeline pipeline = socketChannel.pipeline();
            pipeline.addLast(new LineBasedFrameDecoder(28));
            pipeline.addLast(new StringDecoder());
            pipeline.addLast(new TcpTimeClientHandler());
        }
    }
}
