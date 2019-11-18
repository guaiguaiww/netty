package com.hww.netty.privateprotocol.handler;

import com.hww.netty.privateprotocol.common.Header;
import com.hww.netty.privateprotocol.common.MessageType;
import com.hww.netty.privateprotocol.common.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/11/13
 * @Time: 17:50
 * Description:
 */
public class HeartBeatRespHandler extends ChannelHandlerAdapter {




    private Logger logger = Logger.getLogger(HeartBeatRespHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        NettyMessage message = (NettyMessage) msg;
        // 返回心跳应答消息
        if (message.getHeader() != null  && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()) {
            logger.info("server has Received client heart beat message is: ---> "+ message);
            NettyMessage heartBeat = buildHeatBeat();
            logger.info("Send heart beat response message to client : ---> " + heartBeat);
            ctx.writeAndFlush(heartBeat);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildHeatBeat() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        message.setHeader(header);
        return message;
    }

}
