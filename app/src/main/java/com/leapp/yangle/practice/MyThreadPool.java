package com.leapp.yangle.practice;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadPool {

    public static void main(String[] args) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                    }
                }
        ).start();

//        ThreadPoolExecutor executor = new ThreadPoolExecutor(
//                1,1,60,
//                TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>());


        // 缓存线程 Executors.newCachedThreadPool();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                0, Integer.MAX_VALUE, 60,
                TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
        for (int i = 0; i < 20; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        System.out.println("当前线程是: >>" + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
