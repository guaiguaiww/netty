package com.hww.netty.decoderAndEncoder.delimiterBaseFrameDecoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.apache.log4j.Logger;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/10/30
 * @Time: 11:24
 * Description:
 */
public class EchoClient {

    private Logger logger = Logger.getLogger(EchoClient.class);

    public static void main(String[] args) {
        int port = 8888;
        new EchoClient().connect(port, "127.0.0.1");
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
        protected void initChannel(SocketChannel socketChannel)  {
            //自定义分隔符
            ByteBuf delimiter=Unpooled.copiedBuffer("$_".getBytes());
            //获取管道
            ChannelPipeline pipeline = socketChannel.pipeline();
            /**
             * DelimiterBasedFrameDecoder:分隔符解码器
             * 可以自动的对采用分隔符做流的结束标志的消息进行解码
             */
            pipeline.addLast(new DelimiterBasedFrameDecoder(1024,delimiter));
            pipeline.addLast(new StringDecoder());
            pipeline.addLast(new EchoClientHandler());
        }
    }
}
