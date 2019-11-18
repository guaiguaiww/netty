package com.hww.netty.protobuf.handler;

import com.hww.netty.protobuf.proto.SubscribeRegProto;
import com.hww.netty.protobuf.proto.SubscribeRespProto;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

public class SubRegServerHandler extends ChannelHandlerAdapter {

    private Logger logger = Logger.getLogger(SubRegServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeRegProto.SubscribeReg subscribeReg = (SubscribeRegProto.SubscribeReg) msg;
        int subRegId = subscribeReg.getSubRegId();
        logger.info("server has received request from client is: [ " + subscribeReg.toString() + " ]");
        ctx.writeAndFlush(response(subRegId));
    }

    /**
     * 构造应答对象消息
     * @param subRegId
     * @return
     */
    private SubscribeRespProto.SubscribeResp response(int subRegId) {
        SubscribeRespProto.SubscribeResp.Builder respBuilder = SubscribeRespProto.SubscribeResp.newBuilder();
        respBuilder.setSubRespId(subRegId);
        respBuilder.setRespCode(12);
        respBuilder.setDesc("hello i am server");
        return respBuilder.build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
