package com.hww.io.javaserialize;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/10/30
 * @Time: 16:55
 * Description:
 */
public class UserInfo implements Serializable {

    private String userName;

    private Integer userId;


    public UserInfo buildUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public UserInfo buildUserId(Integer userId) {
        this.userId = userId;
        return this;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public byte[] codeC() {

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        byte[] userNameValue = this.userName.getBytes();

        buffer.put(userNameValue);

        buffer.putInt(this.userId);

        buffer.flip();

        byte[] result = new byte[buffer.remaining()];

        buffer.get(result);
        return result;
    }
}
