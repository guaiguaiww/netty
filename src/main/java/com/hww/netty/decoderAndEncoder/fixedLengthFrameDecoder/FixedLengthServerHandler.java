package com.hww.netty.decoderAndEncoder.fixedLengthFrameDecoder;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/10/30
 * @Time: 14:03
 * Description:
 */
public class FixedLengthServerHandler extends ChannelHandlerAdapter {

    private Logger logger = Logger.getLogger(FixedLengthServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        logger.info("the server has received client's message is :" + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
