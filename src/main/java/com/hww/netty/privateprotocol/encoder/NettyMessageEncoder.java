package com.hww.netty.privateprotocol.encoder;

import com.hww.netty.privateprotocol.common.NettyMessage;
import com.hww.netty.privateprotocol.marshall.MarshallingCodecFactory;
import com.hww.netty.privateprotocol.marshall.NettyMarshallingEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/11/13
 * @Time: 16:50
 * Description:
 */
public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {

    private NettyMarshallingEncoder marshallingEncoder;

    public NettyMessageEncoder() throws IOException {
        this.marshallingEncoder =  MarshallingCodecFactory.buildMarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {
        if (null == msg || null == msg.getHeader()) {
            throw new Exception("The encode message is null");
        }
        ByteBuf sendBuf = Unpooled.buffer();
        /*************************************************写入消息体-start*******************************************/
        sendBuf.writeInt((msg.getHeader().getCrcCode()));
        //---写入length---
        sendBuf.writeInt((msg.getHeader().getLength()));
        //---写入sessionId---
        sendBuf.writeLong((msg.getHeader().getSessionID()));
        //---写入type---
        sendBuf.writeByte((msg.getHeader().getType()));
        //---写入priority---
        sendBuf.writeByte((msg.getHeader().getPriority()));
        if (msg.getHeader().getAttachment()!=null){
            //---写入附件大小---
            sendBuf.writeInt((msg.getHeader().getAttachment().size()));
            for (Map.Entry<String, Object> item: msg.getHeader().getAttachment().entrySet()) {
                String key = item.getKey();
                byte[] keyBytes= key.getBytes("UTF-8");
                sendBuf.writeInt(keyBytes.length);
                sendBuf.writeBytes(keyBytes);
                Object value = item.getValue();
                marshallingEncoder.encode(ctx,value, sendBuf);
            }
        }

        /*************************************************写入消息体-end*******************************************/
        if (msg.getBody() != null) {
            marshallingEncoder.encode(ctx,msg.getBody(), sendBuf);
        } else {
           // sendBuf.writeInt(0);
        }
        //更新报文头里的长度， 在第4个字节出写入Buffer的长度
        sendBuf.setInt(4, sendBuf.readableBytes() );
        // 把Message添加到List传递到下一个Handler
        out.add(sendBuf);
    }
}
