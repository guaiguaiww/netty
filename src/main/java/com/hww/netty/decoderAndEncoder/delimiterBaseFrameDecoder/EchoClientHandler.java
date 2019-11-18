package com.hww.netty.decoderAndEncoder.delimiterBaseFrameDecoder;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/10/30
 * @Time: 11:26
 * Description:
 */
public class EchoClientHandler extends ChannelHandlerAdapter {

    private Logger logger = Logger.getLogger(EchoClientHandler.class);

    private Integer counter=0;


    private static final String ECHO_MESSAGE = "hi welcome to study netty.$_";


    public EchoClientHandler() {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 100; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_MESSAGE.getBytes()));
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        counter=counter+1;
        logger.info("this is "  +counter + " times receive server : [ " + msg + " ]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
