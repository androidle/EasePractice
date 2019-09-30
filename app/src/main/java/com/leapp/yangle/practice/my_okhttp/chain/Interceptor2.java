package com.leapp.yangle.practice.my_okhttp.chain;

import com.leapp.yangle.practice.my_okhttp.Response2;

import java.io.IOException;

public interface Interceptor2 {

    Response2 intercept(Chain2 chain) throws IOException;
}
