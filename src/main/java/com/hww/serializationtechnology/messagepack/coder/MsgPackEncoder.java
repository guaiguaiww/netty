package com.hww.serializationtechnology.messagepack.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/10/31
 * @Time: 9:03
 * Description:编码器
 * MessageToByteEncoder<Object> ==负责将Object类型的POJO对象编码为byte数组，然后写入到ByteBuf中
 */
public class MsgPackEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object message, ByteBuf byteBuf) throws Exception {

        //创建序列化器
        MessagePack messagePack = new MessagePack();
        //序列化
        byte[] serializableValue = messagePack.write(message);
        //写入到ByteBuf中
        byteBuf.writeBytes(serializableValue);
    }
}
