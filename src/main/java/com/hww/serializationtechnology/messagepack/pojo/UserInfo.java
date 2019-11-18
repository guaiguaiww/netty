package com.hww.serializationtechnology.messagepack.pojo;

import org.msgpack.annotation.Message;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/10/31
 * @Time: 10:00
 * Description:
 */
@Message
public class UserInfo {

    private Integer age;
    private String name;


    public UserInfo() {

    }

    public UserInfo(Integer age, String name) {
        this.age = age;
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "age='" + age + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
