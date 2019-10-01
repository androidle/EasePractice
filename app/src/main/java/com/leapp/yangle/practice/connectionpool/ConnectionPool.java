package com.leapp.yangle.practice.connectionpool;

import android.support.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConnectionPool {


    /**
     * 每隔一分钟检查连接池里面的连接是否可用
     * 不可用,就会移除,最大允许闲置时间
     */
    private long keepAlive;

    private boolean cleanFrag;

    // 双端对列,用于存放HttpConnection
    private Deque<HttpConnection> httpConnectionDeque = new ArrayDeque<>();

    public ConnectionPool() {
        this(1, TimeUnit.MINUTES);
    }


    public ConnectionPool(long keepAlive, TimeUnit timeUnit) {
        keepAlive = timeUnit.toMillis(keepAlive);
    }

    private Runnable cleanRunnable = new Runnable() {
        @Override
        public void run() {
            // 当前时间
            while (true) {
                long nextCleanTime = clean(System.currentTimeMillis());
                if (nextCleanTime == -1) {
                    return;
                }

                if (nextCleanTime > 0) {
                    synchronized (ConnectionPool.this) {
                        try {
                            ConnectionPool.this.wait(nextCleanTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    };

    private synchronized long clean(long currentTimeMillis) {
        long maxIdleTime = -1;
        Iterator<HttpConnection> iterator = httpConnectionDeque.iterator();
        while (iterator.hasNext()) {
            HttpConnection httpConnection = iterator.next();

            long idleTime = currentTimeMillis - httpConnection.hasUseTime;
            if (idleTime > keepAlive) {
                iterator.remove();
                // 关闭socket
                httpConnection.closeSocket();
                continue;// 继续检查
            }

            // 记录最长闲置时间
            if (maxIdleTime < idleTime) {
                maxIdleTime = idleTime;
            }

        }

        if (maxIdleTime != -1) {
            return keepAlive - maxIdleTime;
        }

        return maxIdleTime;
    }

    private Executor threadPoolExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new ThreadFactory() {
        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("connection Pool");
            thread.setDaemon(true);
            return thread;
        }
    });

    public synchronized void putConnection(HttpConnection httpConnection) {
        if (!cleanFrag) {
            cleanFrag = true;
            threadPoolExecutor.execute(cleanRunnable);
        }

        httpConnectionDeque.add(httpConnection);
    }

    public synchronized HttpConnection getConnection(String host, int port) {
        Iterator<HttpConnection> iterator = httpConnectionDeque.iterator();
        while (iterator.hasNext()) {
            HttpConnection httpConnection = iterator.next();
            if (httpConnection.isSameConnectionAction(host, port)) {// 匹配到了,可以复用
                iterator.remove();
                return httpConnection;
            }
        }

        return null;
    }

}
