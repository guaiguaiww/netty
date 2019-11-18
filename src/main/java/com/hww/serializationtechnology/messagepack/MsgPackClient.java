package com.hww.serializationtechnology.messagepack;

import com.hww.serializationtechnology.messagepack.coder.MsgPackDecoder;
import com.hww.serializationtechnology.messagepack.coder.MsgPackEncoder;
import com.hww.serializationtechnology.messagepack.handler.MsgPackClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.apache.log4j.Logger;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/10/31
 * @Time: 8:53
 * Description:
 */
public class MsgPackClient {
    private Logger logger = Logger.getLogger(MsgPackClient.class);
    private final String host;
    private final Integer port;
    private final Integer sendNumber;

    public static void main(String[] args) {
        int port = 9000;
        new MsgPackClient("127.0.0.1", port, 5).run();
    }

    public MsgPackClient(String host, Integer port, Integer sendNumber) {
        this.host = host;
        this.port = port;
        this.sendNumber = sendNumber;
    }

    public void run() {
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workGroup).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .handler(new ChildChannelInitializer());
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            logger.info("the client started port in  :" + "[ " + port + " ] and host in [ " + host + " ]");
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
        }
    }

    private class ChildChannelInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            ChannelPipeline pipeline = socketChannel.pipeline();
            pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
            pipeline.addLast("msgPackDecoder", new MsgPackDecoder());
            pipeline.addLast("frameEncoder", new LengthFieldPrepender(2));
            pipeline.addLast("msgPackEncoder", new MsgPackEncoder());
            pipeline.addLast(new MsgPackClientHandler(sendNumber));
        }
    }
}
