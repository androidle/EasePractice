package com.leapp.yangle.practice.my_okhttp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class SocketRequestServer {

    public static final String K = " ";
    public static final String GRNK = "\r\n";
    public static final String VERSION = "HTTP/1.1";

    public String getHost(Request2 request2) {
        try {
            URL url = new URL(request2.getUrl());
            return url.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getPort(Request2 request2) {
        try {
            URL url = new URL(request2.getUrl());
            int port =  url.getPort();
            return port == -1 ? url.getDefaultPort():port;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public String getRequestHeaderAll(Request2 request2) {
        URL url = null;
        try {
            url = new URL(request2.getUrl());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String file = url.getFile();

        // todo 拼接请求头的 请求行
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(request2.getMethod())
                .append(K)
                .append(file)
                .append(K)
                .append(VERSION)
                .append(GRNK);


        // todo 拼接 请求集
        Map<String, String> headers = request2.getHeaders();
        if (!headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append(":")
                        .append(K)
                        .append(entry.getValue())
                        .append(GRNK);
            }

            // 拼接请求空行
            stringBuffer.append(GRNK);
        }

        // todo 拼接 请求体
        if ("POST" .equalsIgnoreCase(request2.getMethod())) {
            stringBuffer.append(request2.requestBody().getBody()).append(GRNK);
        }

        return stringBuffer.toString();
    }

    public String getHttpOrHttps(String urlStr) {
        try {
            URL url = new URL(urlStr);
            return url.getProtocol();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
