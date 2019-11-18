package com.hww.io.bio.second.timeserver;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/9/24
 * @Time: 23:25
 * Description:
 */
public class TimeServerHandlerExecutePool {

    private ExecutorService executorService;



    public TimeServerHandlerExecutePool(int maxPoolSize, int queueSize) {
        /**
         * corePoolSize:Java虚拟机可用的处理器数量,保留在线程池中的线程数量
         * maximumPoolSize: 线程池中允许的最大线程数
         * keepAliveTime：就是非核心线程可以保留的最长的空闲时间
         * TimeUnit：计算这个时间的一个单位
         * BlockingQueue<Runnable>：等待队列，任务可以储存在任务队列中等待被执行，执行的是FIFIO原则
         */
        executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxPoolSize, 120L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize));
    }

    public void execute(Runnable task) {
        executorService.execute(task);
    }
}
