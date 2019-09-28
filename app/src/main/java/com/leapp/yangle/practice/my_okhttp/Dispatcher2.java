package com.leapp.yangle.practice.my_okhttp;

import android.support.annotation.NonNull;

import com.leapp.yangle.practice.my_okhttp.RealCall2.AsyncCall2;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class Dispatcher2 {

    private int maxRequests = 64;
    private int maxRequestsPerHost = 5;

    private final Deque<AsyncCall2> readyAsyncCalls = new ArrayDeque<>();

    private final Deque<AsyncCall2> runningAsyncCalls = new ArrayDeque<>();

    private ExecutorService executorService;

    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), new ThreadFactory() {
                @Override
                public Thread newThread(@NonNull Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("自定义的线程....");
                    thread.setDaemon(false);
                    return thread;
                }
            });
        }
        return executorService;
    }

    public void enqueue(RealCall2.AsyncCall2 call) {
        if (runningAsyncCalls.size() < maxRequests && runningCallsForHost(call) < maxRequestsPerHost) {
            runningAsyncCalls.add(call);
            executorService().execute(call);
        } else {
            readyAsyncCalls.add(call);
        }
    }

    private int runningCallsForHost(AsyncCall2 call) {
        int result = 0;

        SocketRequestServer server = new SocketRequestServer();

        for (AsyncCall2 c : runningAsyncCalls) {
            if (server.getHost(c.getRequest()).equals(server.getHost(call.getRequest()))) result++;
        }
        return result;
    }

    public void finished(AsyncCall2 asyncCall2) {
        runningAsyncCalls.remove(asyncCall2);

        if (readyAsyncCalls.isEmpty()) {
            return;
        }

        for (AsyncCall2 readyAsyncCall : readyAsyncCalls) {
            readyAsyncCalls.remove(readyAsyncCall);

            runningAsyncCalls.add(readyAsyncCall);

            executorService.execute(readyAsyncCall);
        }
    }
}
