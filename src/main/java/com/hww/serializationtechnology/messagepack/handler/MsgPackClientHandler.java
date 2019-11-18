package com.hww.serializationtechnology.messagepack.handler;

import com.hww.serializationtechnology.messagepack.pojo.UserInfo;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/10/31
 * @Time: 9:08
 * Description:
 */
public class MsgPackClientHandler extends ChannelHandlerAdapter {



    private Logger logger = Logger.getLogger(MsgPackClientHandler.class);


    private final Integer sendNumber;

    public MsgPackClientHandler(Integer sendNumber) {
        this.sendNumber = sendNumber;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)  {
        UserInfo[] inFos = getUsers();
        //循环写入的同时也会循坏调用编码器
        for (UserInfo userInfo : inFos) {
            ctx.write(userInfo);
        }
        ctx.flush();

    }


    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
        logger.info("Client receive the msgPack message : " + msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        ctx.flush();
    }

    private UserInfo[] getUsers() {
        UserInfo[] userInFos = new UserInfo[sendNumber];
        UserInfo userInfo = null;
        for (int i = 0; i < sendNumber; i++) {
            userInfo = new UserInfo();
            userInfo.setAge(i);
            userInfo.setName("ABCDEFG ---->" + i);
            userInFos[i] = userInfo;
        }
        return userInFos;
    }
}
