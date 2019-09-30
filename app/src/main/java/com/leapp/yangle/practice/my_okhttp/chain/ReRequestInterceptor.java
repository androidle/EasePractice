package com.leapp.yangle.practice.my_okhttp.chain;

import com.leapp.yangle.practice.my_okhttp.OkHttpClient2;
import com.leapp.yangle.practice.my_okhttp.RealCall2;
import com.leapp.yangle.practice.my_okhttp.Response2;

import java.io.IOException;

/**
 * 重试拦截器
 */
public class ReRequestInterceptor implements Interceptor2 {


    @Override
    public Response2 intercept(Chain2 chain) throws IOException {
        System.out.println("重试拦截器, 执行了...");

        ChainManager chainManager = (ChainManager) chain;
        RealCall2 realCall2 = chainManager.getCall();
        OkHttpClient2 okHttpClient2 = realCall2.getOkHttpClient2();


        IOException ioException = null;
        int retryCount = okHttpClient2.retryCount();
        if (retryCount != 0) {
            for (int i = 0; i < retryCount; i++) {
                try {
                    System.out.println("重试拦截器, 准备返回response了");
                    Response2 response2 = chain.proceed(chainManager.request());//执行下一个拦截器
                    return response2;
                } catch (IOException e) {
                    e.printStackTrace();
                    ioException = e;
                }
            }
        }

        throw ioException;
    }

}
