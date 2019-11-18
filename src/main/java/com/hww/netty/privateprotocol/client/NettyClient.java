package com.hww.netty.privateprotocol.client;

import com.hww.netty.privateprotocol.common.NettyConstant;
import com.hww.netty.privateprotocol.decoder.NettyMessageDecoder;
import com.hww.netty.privateprotocol.encoder.NettyMessageEncoder;
import com.hww.netty.privateprotocol.handler.HeartBeatReqHandler;
import com.hww.netty.privateprotocol.handler.LoginAuthReqHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/11/13
 * @Time: 17:57
 * Description:
 */
public class NettyClient {

    private Logger logger = Logger.getLogger(NettyClient.class);

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    EventLoopGroup group = new NioEventLoopGroup();

    public void connect(int port, String host) throws Exception {
        // 配置客户端NIO线程组
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast("MessageDecoder", new NettyMessageDecoder(1024 * 1024, 4, 4,-8,0));
                            ch.pipeline().addLast("MessageEncoder", new NettyMessageEncoder());
                            //ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
                            ch.pipeline().addLast("HeartBeatHandler", new HeartBeatReqHandler());
                            ch.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler());

                        }
                    });
            // 发起异步连接操作,绑定了本地ip和port用于服务端重复登录保护
            ChannelFuture future = b.connect(new InetSocketAddress("127.0.0.1", 8888)/*,  new InetSocketAddress(NettyConstant.LOCALIP, NettyConstant.LOCAL_PORT)*/).sync();
            // 当对应的channel关闭的时候，就会返回对应的channel。
            logger.info("Netty client start ok : " + (NettyConstant.REMOTEIP + " : " + NettyConstant.PORT));
            // Returns the ChannelFuture which will be notified when this channel is closed. This method always returns the same future instance.
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 所有资源释放完成之后，清空资源，再次发起重连操作
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        try {
                            connect(NettyConstant.PORT, NettyConstant.REMOTEIP);// 发起重连操作
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new NettyClient().connect(NettyConstant.PORT, NettyConstant.REMOTEIP);
    }
}
