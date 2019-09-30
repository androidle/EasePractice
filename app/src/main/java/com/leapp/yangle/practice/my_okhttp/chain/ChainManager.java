package com.leapp.yangle.practice.my_okhttp.chain;

import com.leapp.yangle.practice.my_okhttp.RealCall2;
import com.leapp.yangle.practice.my_okhttp.Request2;
import com.leapp.yangle.practice.my_okhttp.Response2;

import java.io.IOException;
import java.util.List;

public class ChainManager implements Chain2{

    private final List<Interceptor2> interceptors;
    private final int index;
    private final Request2 request;
    private final RealCall2 call;

    public ChainManager(List<Interceptor2> interceptors, int index, Request2 request, RealCall2 call) {
        this.interceptors = interceptors;
        this.index = index;
        this.request = request;
        this.call = call;
    }

    @Override
    public Request2 request() {
        return request;
    }

    @Override
    public Response2 proceed(Request2 request) throws IOException {
        if (index >= interceptors.size()) throw new AssertionError();

        if (interceptors.isEmpty()) {
            throw new IllegalArgumentException("interceptors is empty ");
        }

        Interceptor2 interceptor = interceptors.get(index);

        ChainManager next = new ChainManager(interceptors, index + 1, request, call);

        Response2 response = interceptor.intercept(next);

        return response;
    }

    public List<Interceptor2> getInterceptors() {
        return interceptors;
    }

    public int getIndex() {
        return index;
    }

    public RealCall2 getCall() {
        return call;
    }
}
