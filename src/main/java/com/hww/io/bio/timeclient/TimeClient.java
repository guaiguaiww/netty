package com.hww.io.bio.timeclient;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/9/25
 * @Time: 9:47
 * Description:
 */
public class TimeClient {

    private static Logger logger = Logger.getLogger(TimeClient.class);

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            socket = new Socket("127.0.0.1", port);
            //使用socket建立输入输出流
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            //向服务端写入数据
            out.println("QUERY TIME ORDER");
            logger.info("Send order to server successed");

            String response = in.readLine();
            logger.info("the client has received message form server is " + response);
        } catch (Exception e) {

        } finally {
            if (in != null) {
                try {
                    in.close();
                    in=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket!=null){
                try {
                    socket.close();
                    socket=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
