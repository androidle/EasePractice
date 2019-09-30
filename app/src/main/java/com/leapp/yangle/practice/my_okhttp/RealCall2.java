package com.leapp.yangle.practice.my_okhttp;

import com.leapp.yangle.practice.my_okhttp.chain.ChainManager;
import com.leapp.yangle.practice.my_okhttp.chain.ConnectionServerInterceptor;
import com.leapp.yangle.practice.my_okhttp.chain.Interceptor2;
import com.leapp.yangle.practice.my_okhttp.chain.ReRequestInterceptor;
import com.leapp.yangle.practice.my_okhttp.chain.RequestHeaderInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RealCall2 implements Call2 {

    private OkHttpClient2 okHttpClient2;
    private Request2 request2;

    public RealCall2(OkHttpClient2 okHttpClient2, Request2 request2) {
        this.okHttpClient2 = okHttpClient2;
        this.request2 = request2;
    }

    private boolean executed;
    @Override
    public void enqueue(Callback2 responseCallback) {
        synchronized (this) {
            if (executed) throw new IllegalStateException("Already Executed");
            executed = true;
        }

        okHttpClient2.dispatcher().enqueue(new AsyncCall2(responseCallback));
    }


    final class AsyncCall2 implements Runnable{

        private Callback2 responseCallback;

        public AsyncCall2(Callback2 responseCallback) {
            this.responseCallback = responseCallback;
        }

        public Request2 getRequest() {
            return request2;
        }

        @Override
        public void run() {
            boolean signalledCallback = false;
            try {
                Response2 response = getResponseWithInterceptorChain();
                if (okHttpClient2.isCanceled()) {
                    signalledCallback = true;
                    responseCallback.onFailure(RealCall2.this, new IOException("Canceled"));
                } else {
                    signalledCallback = true;
                    responseCallback.onResponse(RealCall2.this, response);
                }
            } catch (IOException e) {
                if (signalledCallback) {
                    System.out.println("====用户调用过程出错!!!====");
                } else {
                    responseCallback.onFailure(RealCall2.this,new IOException("Okhttp getResponseWithInterceptorChain中出错!!!"));
                }
            } finally {
                okHttpClient2.dispatcher().finished(this);
            }
        }

        private Response2 getResponseWithInterceptorChain() throws IOException {
//            Response2 response2 = new Response2();
//            response2.setBody("test==流程====");
//            return response2;

            List<Interceptor2> interceptor2List = new ArrayList<>();
            interceptor2List.add(new ReRequestInterceptor());// 添加重试拦截器
            interceptor2List.add(new RequestHeaderInterceptor());// 请求头拦截器
            interceptor2List.add(new ConnectionServerInterceptor());// 连接服务器拦截器

            ChainManager chainManager = new ChainManager(interceptor2List,0,request2,RealCall2.this);
            Response2 response2 = chainManager.proceed(request2);

            return response2;
        }
    }

    public OkHttpClient2 getOkHttpClient2() {
        return okHttpClient2;
    }

    public Request2 getRequest2() {
        return request2;
    }
}
