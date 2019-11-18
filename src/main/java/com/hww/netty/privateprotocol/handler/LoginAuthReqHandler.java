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
 * @Time: 18:02
 * Description:
 */
public class LoginAuthReqHandler extends ChannelHandlerAdapter {

    private Logger logger = Logger.getLogger(HeartBeatRespHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端通道激活。。开始发送认证登陆请求报文");
        ctx.writeAndFlush(buildLoginReq());
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        // 如果是握手应答消息，需要判断是否认证成功
        if (message.getHeader() != null   && message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
            byte loginResult = (byte) message.getBody();
            if (loginResult != (byte) 0) {
                // 握手失败，关闭连接
                ctx.close();
            } else {
                logger.info("Login is ok : " + message);
                ctx.fireChannelRead(msg);
            }
        } else
            ctx.close();
    }

    private NettyMessage buildLoginReq() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.value());
        message.setHeader(header);
        message.setBody("it's the request's body");
        logger.info("-------------LoginReqmessage is: "+message);
        return message;
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
        //传递到下一个handler处理
        //ctx.fireExceptionCaught(cause);
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
