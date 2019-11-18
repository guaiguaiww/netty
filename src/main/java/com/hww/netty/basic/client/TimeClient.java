package com.hww.netty.basic.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/10/28
 * @Time: 16:23
 * Description:
 */
public class TimeClient {

    private Logger logger = Logger.getLogger(TimeClient.class);

    public static void main(String[] args) {
        int port = 8888;
        new TimeClient().connect(port, "127.0.0.1");
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
                    .handler(new ChildChannelClientHandler());
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
    private class ChildChannelClientHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            ChannelPipeline pipeline = socketChannel.pipeline();
            pipeline.addLast(new NioTimeClientHandler());
        }
    }
}
