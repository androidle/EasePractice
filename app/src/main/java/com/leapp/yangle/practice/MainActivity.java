package com.leapp.yangle.practice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.leapp.yangle.practice.my_okhttp.Call2;
import com.leapp.yangle.practice.my_okhttp.Callback2;
import com.leapp.yangle.practice.my_okhttp.OkHttpClient2;
import com.leapp.yangle.practice.my_okhttp.Request2;
import com.leapp.yangle.practice.my_okhttp.RequestBody2;
import com.leapp.yangle.practice.my_okhttp.Response2;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String PATH = "http://restapi.amap.com/v3/weather/weatherInfo";

    public static final String GET_PARAMS = "?city=110101&key=13cb58f5884f9749287abbead9c658f2";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void okhttpRequest(View view) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        final Request request = new Request.Builder().url("https://www.baidu.com").build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("okhttp请求失败===>>" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("okhttp请求成功===>>" + response.toString());
            }
        });
    }

    public void myOkhttpRequest(View view) {
        OkHttpClient2 myOkhttpClient = new OkHttpClient2.Builder().build();

        //POST city=110101&key=13cb58f5884f9749287abbead9c658f2
        RequestBody2 requestBody2 = new RequestBody2();
        requestBody2.addBody("city","110101");
        requestBody2.addBody("key","13cb58f5884f9749287abbead9c658f2");

        final Request2 request2 = new Request2.Builder().post(requestBody2).url(PATH).build();

//        final Request2 request2 = new Request2.Builder().get().url(PATH+GET_PARAMS).build();

        Call2 call2 = myOkhttpClient.newCall(request2);

        call2.enqueue(new Callback2() {
            @Override
            public void onFailure(Call2 call, IOException e) {
                System.out.println("自定义okhttp 请求失败====");
            }

            @Override
            public void onResponse(Call2 call, Response2 response2) throws IOException {
                System.out.println("自定义okhttp 请求成功==statusCode=="+ response2.getStatusCode()+"=>" + response2.getBody());
            }
        });
    }
}
