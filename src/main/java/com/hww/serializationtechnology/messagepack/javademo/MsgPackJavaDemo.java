package com.hww.serializationtechnology.messagepack.javademo;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/10/31
 * @Time: 10:07
 * Description:
 */
public class MsgPackJavaDemo {
    public static void main(String[] args) throws IOException {

        System.out.println(new Integer(1) == Integer.valueOf(1));
        List<String> src = new ArrayList<String>();
        src.add("msgpack");
        src.add("kumofs");
        src.add("viver");
        MessagePack msgpack = new MessagePack();
        byte[] raw = msgpack.write(src);
        System.out.println("raw.length = " + raw.length);

        List<String> dst1 = msgpack.read(raw, Templates.tList(Templates.TString));

        for (String dst : dst1) {
            System.out.println(dst);
        }
    }
}
