package com.hww.netty.privateprotocol.common;

import com.hww.netty.privateprotocol.common.Header;
import lombok.Data;
import lombok.ToString;

/**
 * @author hww
 */
@Data
@ToString
public class NettyMessage {

    private Header header;

    private Object body;

}
