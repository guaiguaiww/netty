package com.hww.serializationtechnology.messagepack.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;
import org.msgpack.type.Value;

import java.util.List;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/10/31
 * @Time: 9:02
 * Description:解码器
 */
public class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf> {




    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        //获取ByteBuf中可读的字节数
        int readableLength = byteBuf.readableBytes();

        byte[] value=new byte[readableLength];
        //将管道中的数据读取到value字节数组中去
        byteBuf.getBytes(byteBuf.readerIndex(),value,0,readableLength);
        //创建序列化器
        MessagePack messagePack=new MessagePack();
        //反序列化
        Value serializableValue = messagePack.read(value);
        //加入到管道中
        list.add(serializableValue);
    }
}
