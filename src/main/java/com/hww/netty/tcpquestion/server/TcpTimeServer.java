package com.hww.netty.tcpquestion.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.apache.log4j.Logger;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/10/29
 * @Time: 17:07
 * Description:
 */
public class TcpTimeServer {

    private Logger logger = Logger.getLogger(TcpTimeServer.class);

    public static void main(String[] args) {
        int port = 8889;
        new TcpTimeServer().bind(port);
    }

    public void bind(int port) {
        //用于服务端接受客户端的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //用于进行SocketChannel的网路读写
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建启动服务器的对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //backLog==积压,设置tcp的参数
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //.handler==>bossGroup,设置bossGroup
                    //.handler(new LoggingHandler(LogLevel.INFO))
                    //.childHandle==>workerGroup,设置workerGroup
                    .childHandler(new TcpChildChannelServerHandler());
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

    /**
     * 处理io事件
     */
    private class TcpChildChannelServerHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            //获取管道
            ChannelPipeline pipeline = socketChannel.pipeline();
            /**解码器
             * LineBasedFrameDecoder：以换行符作为结束标志的解码器，支持配置单行的最大长度如果连续读取到最大长度后
             * 任然没有发现换行符就会抛出异常，同时忽略掉之前读取的异常码流。
             * 工作原理：它依次遍历byteBuf中的可读字节，判断看是否有"\n"|"\r\n",
             * 如果有，就以此位置作为结束的位置，从可读索引到结束的位置区间的字节就组成了一行
             */
            pipeline.addLast(new LineBasedFrameDecoder(20));
            //将接收到的对象转换成字符串
            pipeline.addLast(new StringDecoder());
            pipeline.addLast(new TcpTimeServerHandler());

        }
    }
}
