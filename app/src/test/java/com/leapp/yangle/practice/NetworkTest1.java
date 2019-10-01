package com.leapp.yangle.practice;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

import javax.net.ssl.SSLSocketFactory;

public class NetworkTest1 {

    public static final String PATH = "http://restapi.amap.com/v3/weather/weatherInfo?city=110101&key=13cb58f5884f9749287abbead9c658f2";

    @Test
    public void testURL()  {
        try {
            URL url = new URL(PATH);
            System.out.println("Protocol:"+url.getProtocol());
            System.out.println("Host    :"+url.getHost());
            System.out.println("File    :"+url.getFile());
            System.out.println("Query   :"+url.getQuery());
            System.out.println("Path    :"+url.getPath());
            System.out.println("DefPort :"+url.getDefaultPort());//http :80 https:443
            System.out.println("Port    :"+url.getPort());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_socketHttp_get() {
        try {

            /**
             * GET /v3/weather/weatherInfo?city=110101&key=13cb58f5884f9749287abbead9c658f2 HTTP/1.1
             * Host: restapi.amap.com
             */
            Socket socket = new Socket("restapi.amap.com", 80);

            // 写数据 请求
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            /**
             * GET / HTTP/1.1
             * Host: www.baidu.com
             */
            bw.write("GET /v3/weather/weatherInfo?city=110101&key=13cb58f5884f9749287abbead9c658f2 HTTP/1.1\r\n");
            bw.write("Host: restapi.amap.com\r\n");
            bw.write("\r\n");//请求头 空行
            bw.flush();

            // 读数据 响应
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String readLine = null;

            while ((readLine = br.readLine()) != null) {
                System.out.println("响应的数据:" + readLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_socketHttp_post() {
        try {

            /**
             * POST /v3/weather/weatherInfo HTTP/1.1
             * host: restapi.amap.com
             * Content-Length: 48
             * Content-Type: application/x-www-form-urlencoded
             * key=13cb58f5884f9749287abbead9c658f2&city=110101
             */
            Socket socket = new Socket("restapi.amap.com", 80);

            // 写数据 请求
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            /**
             * GET / HTTP/1.1
             * Host: www.baidu.com
             */
            bw.write("POST /v3/weather/weatherInfo HTTP/1.1\r\n");
            bw.write("host: restapi.amap.com\r\n");
            bw.write("Content-Length: 48\r\n");
            bw.write("Content-Type: application/x-www-form-urlencoded\r\n");
            bw.write("\r\n");//请求头 空行
            // Post请求体
            bw.write("key=13cb58f5884f9749287abbead9c658f2&city=110101\r\n");
            bw.flush();

            // 读数据 响应
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String readLine = null;

            while ((readLine = br.readLine()) != null) {
                System.out.println("响应的数据:" + readLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_socketHttps() {
        try {
            // https SSL层握手
            Socket socket = SSLSocketFactory.getDefault().createSocket("www.baidu.com", 443);

            // 写数据 请求
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            /**
             * GET / HTTP/1.1
             * Host: www.baidu.com
             */
            bw.write("GET / HTTP/1.1\r\n");
            bw.write("Host: www.baidu.com\r\n");
            bw.write("\r\n");//请求头 空行
            bw.flush();

            // 读数据 响应
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String readLine = null;

            while ((readLine = br.readLine()) != null) {
                System.out.println("响应的数据:" + readLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
