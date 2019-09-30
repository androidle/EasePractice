package com.leapp.yangle.practice.my_okhttp;

public class OkHttpClient2 {

    Dispatcher2 dispatcher;
    private boolean isCanceled;
    private int retryCount;

    public OkHttpClient2() {
        this(new Builder());
    }

    public OkHttpClient2(Builder builder) {
        this.dispatcher = builder.dispatcher;
        this.isCanceled = builder.isCanceled;
        this.retryCount = builder.retryCount;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public Call2 newCall(Request2 request) {
        return new RealCall2(this,request);
    }

    public Dispatcher2 dispatcher() {
        return dispatcher;
    }

    public int retryCount() {
        return retryCount;
    }

    public static final class Builder{

        Dispatcher2 dispatcher;
        private boolean isCanceled;
        private int retryCount = 3;

        public Builder() {
            dispatcher = new Dispatcher2();
        }

        public Builder dispatcher(Dispatcher2 dispatcher) {
            this.dispatcher = dispatcher;
            return this;
        }

        public Builder canceled() {
            this.isCanceled = true;
            return this;
        }

        public Builder retryCount(int retryCount) {
            this.retryCount = retryCount;
            return this;
        }

        public OkHttpClient2 build() {
            return new OkHttpClient2(this);
        }
    }
}
