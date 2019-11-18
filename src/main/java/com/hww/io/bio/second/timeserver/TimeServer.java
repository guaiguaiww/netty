package com.hww.io.bio.second.timeserver;

import com.hww.io.bio.timehandler.TimeServerHandler;
import org.apache.log4j.Logger;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/9/24
 * @Time: 23:17
 * Description:伪异步io编程-其底层的通信机制依然使用的是同步阻塞io
 * 当有新的客户端接入的时候，将客户端的Socket封装成一个Task,传递到后端的线程池中进行处理。
 * jDK的线程池维护一个消总队列和N个活跃线程 ，
 * 对消总队列中的任务进行处理。由于线程池可以设置消息队列的大小和最大
 * 线程数 ，因此它的资源占用是可控的无沦多少个客户端并发访问都不会导致资源的耗尽和宕机。
 */
public class TimeServer {

    private static Logger logger = Logger.getLogger(TimeServer.class);

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            logger.info("the server has started in " + port);
            Socket socket = null;
            //创建线程池
            TimeServerHandlerExecutePool executePool = new TimeServerHandlerExecutePool(50, 10000);
            while (true) {
                socket = serverSocket.accept();
                //将socket封装成为一个task,使用线程池去处理
                executePool.execute(new TimeServerHandler(socket));
            }
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
                serverSocket = null;
            }
        }
    }
}
