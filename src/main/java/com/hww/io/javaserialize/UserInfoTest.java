package com.hww.io.javaserialize;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/10/30
 * @Time: 17:07
 * Description:
 */
public class UserInfoTest {

    public static void main(String[] args)  throws Exception{
        UserInfo info =new UserInfo();
        info.buildUserName("welcome to netty").buildUserId(100);
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        ObjectOutputStream os=new ObjectOutputStream(bos);
        os.writeObject(info);
        os.flush();
        os.close();

        byte[] b = bos.toByteArray();
        System.out.println("the jdk serializable length is :"+ b.length);
        bos.close();
        System.out.println("-----------------------------------------------------");




        System.out.println("the byte array serializable length is : "+info.codeC().length);

    }
}
