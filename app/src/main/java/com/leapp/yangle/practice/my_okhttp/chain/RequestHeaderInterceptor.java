package com.leapp.yangle.practice.my_okhttp.chain;

import com.leapp.yangle.practice.my_okhttp.Request2;
import com.leapp.yangle.practice.my_okhttp.RequestBody2;
import com.leapp.yangle.practice.my_okhttp.Response2;
import com.leapp.yangle.practice.my_okhttp.SocketRequestServer;

import java.io.IOException;
import java.util.Map;

public class RequestHeaderInterceptor implements Interceptor2 {

    @Override
    public Response2 intercept(Chain2 chain2) throws IOException {
        System.out.println("请求头拦截器, 执行了...");
        // 拼接请求头 集
        ChainManager chainManager = (ChainManager) chain2;
        Request2 request2 = chainManager.request();
        Map<String, String> headerMap = request2.getHeaders();
        // get/post hostname
        headerMap.put("host", new SocketRequestServer().getHost(request2));

        if ("POST" .equalsIgnoreCase(request2.getMethod())) {
            // post请求 有请求体长度,请求体类型
            headerMap.put("Content-Length", String.valueOf(request2.requestBody().getBody().length()));
            headerMap.put("Content-Type", RequestBody2.TYPE);
        }

        return chain2.proceed(request2);//执行下一个拦截器
    }
}
