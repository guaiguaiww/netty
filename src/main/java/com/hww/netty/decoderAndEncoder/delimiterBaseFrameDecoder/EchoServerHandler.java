package com.hww.netty.decoderAndEncoder.delimiterBaseFrameDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/10/30
 * @Time: 11:10
 * Description:
 */
public class EchoServerHandler extends ChannelHandlerAdapter {

    private Logger logger = Logger.getLogger(EchoServerHandler.class);

    private Integer count = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        logger.info("this is " + (++count) + " times received client's :[ " + body + " ]");
        body = "$_" + body;
        ByteBuf resp = Unpooled.copiedBuffer(body.getBytes());
        ctx.writeAndFlush(resp);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
