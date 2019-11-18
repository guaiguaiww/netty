package com.hww.netty.privateprotocol.common;

import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * int a = 0b12;    //0b开头为二进制数
 * int b = 012;     //0开头为八进制数
 * int c = 11;      //默认为十进制数
 * int d = 0x11;    //0x开头为十六进制数
 *
 * netty协议栈使用的数据结构
 * @author hww
 */
@Data
@ToString
public class Header {
    /**
     * Netty消息检验码，由三部分组成
     * 0xabef:固定值，表明该消息是netty的协议消息，2个字节
     * 主版本号：1-255，1个字节
     * 次版本号：1-255，1个字节
     * 2个十六进制 = 1个字节
     */
   private  int crcCode=0xabef0101;
    /**
     *  消息长度==消息头+消息体
     */
    private int length;
    /**
     * 会话ID：集群节点内全局唯一,由会话id生成
     */
    private long sessionID;
    /**
     * 消息类型
     */
    private byte type;
    /**
     * 消息优先级：0~~255
     */
    private byte priority;
    /**
     * 可选字段,用于扩展消息头
     */
    private Map<String,Object> attachment = new HashMap();

}
