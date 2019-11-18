package com.hww.netty.protobuf.handler;

import com.hww.netty.protobuf.proto.SubscribeRegProto;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class SubRegClientHandler extends ChannelHandlerAdapter {

    private Logger logger = Logger.getLogger(SubRegClientHandler.class);

    public SubRegClientHandler() {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        logger.info("Receive server response : [ " + msg + " ]");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            ctx.write(subReg(i));
        }
        ctx.flush();
    }

    private SubscribeRegProto.SubscribeReg subReg(int i) {
        SubscribeRegProto.SubscribeReg.Builder subScribeRegBuilder = SubscribeRegProto.SubscribeReg.newBuilder();
        subScribeRegBuilder.setSubRegId(i);
        subScribeRegBuilder.setUserName("hww");
        subScribeRegBuilder.setProductName("nettyBook");
        List<String> addressList = new ArrayList<>(0);
        addressList.add("sichuan");
        addressList.add("xian");
        addressList.add("baoji");
        subScribeRegBuilder.addAllAddress(addressList);
        return subScribeRegBuilder.build();
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
