package com.leapp.yangle.practice.my_okhttp.chain;

import com.leapp.yangle.practice.my_okhttp.Request2;
import com.leapp.yangle.practice.my_okhttp.Response2;

import java.io.IOException;

public interface Chain2 {

    Request2 request();

    Response2 proceed(Request2 request) throws IOException;

}
