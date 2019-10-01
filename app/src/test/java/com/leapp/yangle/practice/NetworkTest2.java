package com.leapp.yangle.practice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;

import javax.net.ssl.SSLSocketFactory;

public class NetworkTest2 {

    public static void main(String[] args) {
        test_socketHttps();
    }


    public static void test_socketHttps() {
        System.out.println("请输入网址,然后回车....");
        BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
        try {
            String path = br1.readLine();
            URL url = new URL("https://" + path);

            String host = url.getHost();
            String file = url.getFile();
            if (file == null || file.length() == 0) {
                file = "/";
            }

            int port = 0;
            Socket socket = null;
            if ("http" .equalsIgnoreCase(url.getProtocol())) {
                port = 80;
                socket = new Socket(host, port);
            } else if ("https" .equalsIgnoreCase(url.getProtocol())) {
                port = 443;
                socket = SSLSocketFactory.getDefault().createSocket(host, port);
            }

            // 写数据 请求
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            /**
             * GET / HTTP/1.1
             * Host: www.baidu.com
             */
            bw.write("GET " + file + " HTTP/1.1\r\n");
            bw.write("Host: " + host + "\r\n");
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
