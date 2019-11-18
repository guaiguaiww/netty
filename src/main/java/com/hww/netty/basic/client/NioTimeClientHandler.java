package com.hww.netty.basic.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/10/29
 * @Time: 9:22
 * Description:
 *
 *
 *
 */
public class NioTimeClientHandler extends ChannelHandlerAdapter {

    private Logger logger = Logger.getLogger(NioTimeClientHandler.class);


    private ByteBuf firstMessage;

    public NioTimeClientHandler(){
        byte reg[]="QUERY TIME ORDER".getBytes();
        firstMessage=Unpooled.buffer(reg.length);
        firstMessage.writeBytes(reg);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] reg = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(reg);
        String body = new String(reg, "UTF-8");
        logger.info("Now is : " + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn(cause.getMessage());
        ctx.close();
    }
}
