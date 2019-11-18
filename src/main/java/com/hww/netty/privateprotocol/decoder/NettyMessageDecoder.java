package com.hww.netty.privateprotocol.decoder;

import com.hww.netty.privateprotocol.common.Header;
import com.hww.netty.privateprotocol.common.NettyMessage;
import com.hww.netty.privateprotocol.marshall.NettyMarshallingDecoder;
import com.hww.netty.privateprotocol.marshall.MarshallingCodecFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/11/13
 * @Time: 16:50
 * Description:
 */
public class NettyMessageDecoder  extends LengthFieldBasedFrameDecoder {

    private NettyMarshallingDecoder marshallingDecoder;

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,int lengthAdjustment, int initialBytesToStrip) throws IOException{
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
        marshallingDecoder =MarshallingCodecFactory.buildMarshallingDecoder();
    }
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }

        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setCrcCode(frame.readInt());
        header.setLength(frame.readInt());
        header.setSessionID(frame.readLong());
        header.setType(frame.readByte());
        header.setPriority(frame.readByte());

        int size = frame.readInt();
        if (size > 0) {
            Map<String, Object> attch = new HashMap<String, Object>(size);
            for (int i = 0; i < size; i++) {
                int keyLength = frame.readInt();
                byte[]  keyByte = new byte[keyLength];
                frame.readBytes(keyByte);
                String  key = new String(keyByte, "UTF-8");
                attch.put(key, marshallingDecoder.decode(ctx,frame));
            }
            header.setAttachment(attch);
        }
        if (frame.readableBytes()>4) {
            message.setBody(marshallingDecoder.decode(ctx,frame));
        }
        message.setHeader(header);
        return message;
    }
}
