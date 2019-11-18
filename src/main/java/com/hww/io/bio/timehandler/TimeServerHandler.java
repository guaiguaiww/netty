package com.hww.io.bio.timehandler;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/9/24
 * @Time: 23:53
 * Description:
 */
public class TimeServerHandler implements Runnable {
    private Logger logger = Logger.getLogger(TimeServerHandler.class);
    private Socket socket;
    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            /**
             *  InputStream inputStream = socket.getInputStream();
             *  //This method blocks until input data is available, the end of the stream is detected, or an exception is thrown
             *  public int read(byte b[], int off, int len);
             */
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(), true);
            String currentTime = null;
            String body = null;
            while (true) {
                body = in.readLine();
                if (body == null) {
                    break;
                }
                logger.info(" the server has received order is " + body);
                currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                out.println(currentTime);
            }
        } catch (Exception e) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (out != null) {
                out.close();
                out = null;
            }
            if (this.socket != null) {
                try {
                    this.socket.close();
                    this.socket = null;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
