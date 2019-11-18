package com.hww.io.bio.one.timeserver;

import com.hww.io.bio.timehandler.TimeServerHandler;
import org.apache.log4j.Logger;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/9/25
 * @Time: 10:23
 * Description:同步阻塞io通信
 * 采用 BIO 通信的服务端，通常由一个独立的Acceptor线程负责监听客户端的连接
 * 它接收到客户端的连接请求后。为每个客户端创建一个新的线程行链路处理,处理完之后,通过输出流返回应答给客户端,线程销毁。
 * 这就是典型的一请求一应答通信模型。
 *
 * 该模型最大的问题就是缺乏弹性伸缩能力。
 * 当客户端并发访问量增加后,服务端创建的线程个数和客端的并发访问数1:1的正比关系,
 * 由于线程是 Java 虚拟机非宝贵的系统资源。当线程数膨胀之后,系统的性能将急剧下降,
 * 随着并发访问量的继续增大,系统会发生线程堆栈溢出、创建线程失败等问题·并最终导致进程宕机，不能对外提供服务
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
            while (true) {
                //如果没有客户端建立连接，服务端(主线程)会一直阻塞在这里serverSocket.accept()
                socket = serverSocket.accept();
                //针对每一个socket开启一个线程去处理
                new Thread(new TimeServerHandler(socket)).start();
            }
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
                serverSocket = null;
            }
        }
    }
}
