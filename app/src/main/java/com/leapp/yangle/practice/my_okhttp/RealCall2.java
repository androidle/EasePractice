package com.leapp.yangle.practice.my_okhttp;

import java.io.IOException;

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

        private Response2 getResponseWithInterceptorChain() {
            Response2 response2 = new Response2();
            response2.setBody("test==流程====");
            return response2;
        }


    }
}
