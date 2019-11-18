package com.hww.serializationtechnology.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import com.hww.serializationtechnology.protobuf.code.SubscribeRegProto;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/11/1
 * @Time: 9:43
 * Description: java代码测试ProtoBuf
 */
public class ProtobufTest {

    private static Logger logger = Logger.getLogger(ProtobufTest.class);

    /**
     * 编码--调用protoBuf提供的toByteArray()方法编码
     * byte[] body=object.toByteArray()
     */
    private static byte[] encode(SubscribeRegProto.SubscribeReg subscribeReg) {
        return subscribeReg.toByteArray();
    }

    /**
     * 解码--调用protoBuf提供的parseFrom(byte[] body)方法解码
     * Object object=object.parseFrom(body)
     */
    private static SubscribeRegProto.SubscribeReg decode(byte[] body) throws InvalidProtocolBufferException {
        return SubscribeRegProto.SubscribeReg.parseFrom(body);
    }

    private static SubscribeRegProto.SubscribeReg createSubscribeReg() {
        SubscribeRegProto.SubscribeReg.Builder subscribeBuilder = SubscribeRegProto.SubscribeReg.newBuilder();
        subscribeBuilder.setSubRegId(1);
        subscribeBuilder.setProductName("sichuanerha");
        subscribeBuilder.setUserName("songshihan");
        ArrayList<String> addressList = new ArrayList<>();
        addressList.add("ganshu");
        addressList.add("sichuan");
        addressList.add("hangzhou");
        subscribeBuilder.addAllAddress(addressList);
        return subscribeBuilder.build();
    }

    public static void main(String[] args) throws InvalidProtocolBufferException, UnsupportedEncodingException {
        SubscribeRegProto.SubscribeReg subscribeReg1 = createSubscribeReg();
        logger.info("original object : " + subscribeReg1.toString());
        logger.info("-----------------------------");

        byte[] encodeObject = encode(subscribeReg1);
        logger.info(new String(encodeObject, "UTF-8"));


        logger.info("-----------------------------");
        SubscribeRegProto.SubscribeReg subscribeReg2 = decode(encodeObject);
        logger.info("after decode : " + subscribeReg2.toString());


        logger.info("-----------------------------");

        logger.info("Assert equal :----->" + subscribeReg1.equals(subscribeReg2));
    }
}
